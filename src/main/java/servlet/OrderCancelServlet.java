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

public class OrderCancelServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final OrderDao orderDao = new OrderDao();
    private final ItemDao itemDao = new ItemDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("currentOrderNo") != null) {
            response.sendRedirect(request.getContextPath() + "/order/status");
        } else {
            response.sendRedirect(request.getContextPath() + "/top");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer orderId = (Integer) session.getAttribute("currentOrderId");
        if (orderId == null) {
            String paramId = request.getParameter("order_id");
            if (paramId != null && !paramId.trim().isEmpty()) {
                try {
                    orderId = Integer.parseInt(paramId.trim());
                } catch (NumberFormatException ignored) {
                }
            }
        }

        if (orderId == null) {
            response.sendRedirect(request.getContextPath() + "/top");
            return;
        }

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            Order order = orderDao.findByIdForUpdate(conn, orderId);
            if (order == null) {
                conn.rollback();
                session.removeAttribute("currentOrderId");
                session.removeAttribute("currentOrderNo");
                request.setAttribute("errorMessage", "対象の注文は既に削除されています");
                request.setAttribute("modalAction", "redirect");
                request.setAttribute("redirectUrl", request.getContextPath() + "/top");
                request.getRequestDispatcher("/WEB-INF/jsp/top.jsp").forward(request, response);
                return;
            }

            if (!"未対応".equals(order.getDeliveryStatus())) {
                conn.rollback();
                order.setDetails(orderDao.findOrderDetails(conn, orderId));
                request.setAttribute("order", order);
                request.setAttribute("errorMessage", "対応中のため取り消しできませんでした");
                request.setAttribute("modalAction", "reload");
                request.setAttribute("redirectUrl", request.getContextPath() + "/order/status");
                request.getRequestDispatcher("/WEB-INF/jsp/order_status.jsp").forward(request, response);
                return;
            }

            List<OrderDetail> details = orderDao.findOrderDetailsForUpdate(conn, orderId);
            for (OrderDetail detail : details) {
                itemDao.addStock(conn, detail.getItemId(), detail.getQuantity());
            }

            orderDao.deleteOrderDetails(conn, orderId);
            orderDao.deleteOrder(conn, orderId);

            conn.commit();

            session.removeAttribute("currentOrderId");
            session.removeAttribute("currentOrderNo");

            response.sendRedirect(request.getContextPath() + "/top");
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
