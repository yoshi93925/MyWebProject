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

public class AdminItemStockServlet extends HttpServlet {
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
            request.getRequestDispatcher("/WEB-INF/jsp/admin_item_stock.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("item_id");
        String qtyParam = request.getParameter("quantity");

        int itemId;
        int quantity;
        try {
            itemId = Integer.parseInt(idParam);
            quantity = Integer.parseInt(qtyParam.trim());
        } catch (Exception e) {
            request.setAttribute("errorMessage", "入荷数量は半角数字で正しく入力してください。");
            forwardWithItem(request, response, idParam);
            return;
        }

        if (quantity <= 0) {
            request.setAttribute("errorMessage", "入荷数量は1以上を入力してください。");
            forwardWithItem(request, response, idParam);
            return;
        }

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            Item item = itemDao.findByIdForUpdate(conn, itemId);
            if (item == null) {
                conn.rollback();
                request.setAttribute("errorMessage", "対象の物資は既に削除されています");
                request.setAttribute("modalAction", "redirect");
                request.setAttribute("redirectUrl", request.getContextPath() + "/admin/items");
                request.setAttribute("items", itemDao.findAll());
                request.getRequestDispatcher("/WEB-INF/jsp/admin_items.jsp").forward(request, response);
                return;
            }

            itemDao.addStock(conn, itemId, quantity);
            conn.commit();

            HttpSession session = request.getSession();
            session.setAttribute("successMessage", "在庫を入荷しました。");
            response.sendRedirect(request.getContextPath() + "/admin/items");
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {
                }
            }
            throw new ServletException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    private void forwardWithItem(HttpServletRequest request, HttpServletResponse response, String idParam)
            throws ServletException, IOException {
        try {
            int itemId = Integer.parseInt(idParam);
            Item item = itemDao.findById(itemId);
            request.setAttribute("item", item);
        } catch (Exception ignored) {
        }
        request.getRequestDispatcher("/WEB-INF/jsp/admin_item_stock.jsp").forward(request, response);
    }
}
