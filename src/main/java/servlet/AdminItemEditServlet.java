package servlet;

import dao.ItemDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import model.Item;
import util.DBUtil;

public class AdminItemEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ItemDao itemDao = new ItemDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("item_id");
        int itemId;
        try {
            itemId = Integer.parseInt(idParam);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/items");
            return;
        }

        try {
            Item item = itemDao.findById(itemId);
            if (item == null) {
                request.setAttribute("errorMessage", "対象の物資は既に削除されています");
                request.setAttribute("modalAction", "redirect");
                request.setAttribute("redirectUrl", request.getContextPath() + "/admin/items");
                request.setAttribute("items", itemDao.findAll());
                request.getRequestDispatcher("/WEB-INF/jsp/admin_items.jsp").forward(request, response);
                return;
            }

            request.setAttribute("item", item);
            request.getRequestDispatcher("/WEB-INF/jsp/admin_item_edit.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("item_id");
        String genreParam = request.getParameter("genre_id");
        String itemName = request.getParameter("item_name");
        String stockParam = request.getParameter("stock");
        String location = request.getParameter("location");

        if (itemName != null) itemName = itemName.trim();
        if (location != null) location = location.trim();

        int itemId;
        int genreId;
        int stock;
        try {
            itemId = Integer.parseInt(idParam);
            genreId = Integer.parseInt(genreParam);
            stock = Integer.parseInt(stockParam.trim());
        } catch (Exception e) {
            request.setAttribute("errorMessage", "入力内容を確認してください。在庫数は半角数字で入力してください。");
            forwardWithItem(request, response, idParam, genreParam, itemName, stockParam, location);
            return;
        }

        if (itemName == null || itemName.isEmpty() || location == null || location.isEmpty() || stock < 0) {
            request.setAttribute("errorMessage", "全項目を正しく入力してください。");
            forwardWithItem(request, response, idParam, genreParam, itemName, stockParam, location);
            return;
        }

        try {
            if (itemDao.isDuplicate(genreId, itemName, location, itemId)) {
                request.setAttribute("errorMessage", "品名と場所が重複しているため更新できません");
                forwardWithItem(request, response, idParam, genreParam, itemName, stockParam, location);
                return;
            }

            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                conn.setAutoCommit(false);

                Item lockedItem = itemDao.findByIdForUpdate(conn, itemId);
                if (lockedItem == null) {
                    conn.rollback();
                    request.setAttribute("errorMessage", "対象の物資は既に削除されています");
                    request.setAttribute("modalAction", "redirect");
                    request.setAttribute("redirectUrl", request.getContextPath() + "/admin/items");
                    request.setAttribute("items", itemDao.findAll());
                    request.getRequestDispatcher("/WEB-INF/jsp/admin_items.jsp").forward(request, response);
                    return;
                }

                lockedItem.setGenreId(genreId);
                lockedItem.setItemName(itemName);
                lockedItem.setStock(stock);
                lockedItem.setLocation(location);
                itemDao.update(conn, lockedItem);

                conn.commit();

                HttpSession session = request.getSession();
                session.setAttribute("successMessage", "物資情報を更新しました。");
                response.sendRedirect(request.getContextPath() + "/admin/items");
            } catch (SQLException e) {
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException ignored) {
                    }
                }
                throw e;
            } finally {
                if (conn != null) {
                    try {
                        conn.setAutoCommit(true);
                        conn.close();
                    } catch (SQLException ignored) {
                    }
                }
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void forwardWithItem(HttpServletRequest request, HttpServletResponse response,
                                String idParam, String genreParam, String itemName, String stockParam, String location)
            throws ServletException, IOException {
        Item item = new Item();
        try {
            item.setItemId(Integer.parseInt(idParam));
            item.setGenreId(Integer.parseInt(genreParam));
            item.setStock(Integer.parseInt(stockParam));
        } catch (Exception ignored) {
        }
        item.setItemName(itemName);
        item.setLocation(location);
        request.setAttribute("item", item);
        request.getRequestDispatcher("/WEB-INF/jsp/admin_item_edit.jsp").forward(request, response);
    }
}
