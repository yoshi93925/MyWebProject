package servlet;

import dao.ItemDao;
import dao.OrderDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import model.Order;
import model.OrderDetail;
import util.DBUtil;

public class AdminOrderDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final OrderDao orderDao = new OrderDao();
    private final ItemDao itemDao = new ItemDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String tab = request.getParameter("tab");
        if (tab == null || tab.trim().isEmpty()) {
            tab = "all";
        }
        response.sendRedirect(request.getContextPath() + "/admin/orders?tab=" + tab);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("order_id");
        String tab = request.getParameter("tab");
        if (tab == null || tab.trim().isEmpty()) {
            tab = "all";
        }

        int orderId;
        try {
            orderId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/orders?tab=" + tab);
            return;
        }

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            Order order = orderDao.findByIdForUpdate(conn, orderId);
            if (order == null || !"未対応".equals(order.getDeliveryStatus())) {
                conn.rollback();
                request.setAttribute("errorMessage", "対象の注文は既に削除されているか、ステータスが変更されています");
                request.setAttribute("modalAction", "redirect");
                request.setAttribute("redirectUrl", request.getContextPath() + "/admin/orders?tab=" + tab);
                request.setAttribute("orders", orderDao.findAllWithDetails(tab));
                request.setAttribute("currentTab", tab);
                request.getRequestDispatcher("/WEB-INF/jsp/admin_orders.jsp").forward(request, response);
                return;
            }

            List<OrderDetail> details = orderDao.findOrderDetailsForUpdate(conn, orderId);
            for (OrderDetail detail : details) {
                itemDao.addStock(conn, detail.getItemId(), detail.getQuantity());
            }

            orderDao.deleteOrderDetails(conn, orderId);
            orderDao.deleteOrder(conn, orderId);

            conn.commit();

            HttpSession session = request.getSession();
            session.setAttribute("successMessage", "注文データを削除しました。");
            response.sendRedirect(request.getContextPath() + "/admin/orders?tab=" + tab);
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
