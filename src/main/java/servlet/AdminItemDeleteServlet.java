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

public class AdminItemDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ItemDao itemDao = new ItemDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/admin/items");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("item_id");
        int itemId;
        try {
            itemId = Integer.parseInt(idParam);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/items");
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

            if (itemDao.hasOrderHistory(conn, itemId)) {
                conn.rollback();
                request.setAttribute("errorMessage", "注文履歴が存在するため削除できません");
                request.setAttribute("modalAction", "reload");
                request.setAttribute("redirectUrl", request.getContextPath() + "/admin/items");
                request.setAttribute("items", itemDao.findAll());
                request.getRequestDispatcher("/WEB-INF/jsp/admin_items.jsp").forward(request, response);
                return;
            }

            itemDao.delete(conn, itemId);
            conn.commit();

            HttpSession session = request.getSession();
            session.setAttribute("successMessage", "物資を削除しました。");
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
}
