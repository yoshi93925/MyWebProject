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
import java.util.Comparator;
import java.util.List;
import model.CartItem;
import model.Item;
import model.Order;
import model.OrderDetail;
import util.CsrfTokenUtil;
import util.DBUtil;
import util.OrderNoGenerator;

public class OrderConfirmServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ItemDao itemDao = new ItemDao();
    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute("requestedItems");
        Order orderInput = (Order) session.getAttribute("orderInput");

        if (cart == null || cart.isEmpty() || orderInput == null) {
            response.sendRedirect(request.getContextPath() + "/order");
            return;
        }

        request.setAttribute("cart", cart);
        request.setAttribute("orderInput", orderInput);
        CsrfTokenUtil.generateToken(request);

        request.getRequestDispatcher("/WEB-INF/jsp/order_confirm.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute("requestedItems");
        Order orderInput = (Order) session.getAttribute("orderInput");

        if (cart == null || cart.isEmpty() || orderInput == null) {
            request.setAttribute("errorMessage", "セッションが切れました。最初からやり直してください。");
            request.setAttribute("modalAction", "redirect");
            request.setAttribute("redirectUrl", request.getContextPath() + "/order");
            request.getRequestDispatcher("/WEB-INF/jsp/order_confirm.jsp").forward(request, response);
            return;
        }

        if (!CsrfTokenUtil.validateToken(request)) {
            request.setAttribute("cart", cart);
            request.setAttribute("orderInput", orderInput);
            request.setAttribute("errorMessage", "不正なリクエストまたは二重送信です。");
            request.setAttribute("modalAction", "redirect");
            request.setAttribute("redirectUrl", request.getContextPath() + "/order");
            request.getRequestDispatcher("/WEB-INF/jsp/order_confirm.jsp").forward(request, response);
            return;
        }

        cart.sort(Comparator.comparingInt(CartItem::getItemId));

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            for (CartItem ci : cart) {
                Item item = itemDao.findByIdForUpdate(conn, ci.getItemId());
                if (item == null || item.getStock() < ci.getQuantity()) {
                    conn.rollback();
                    request.setAttribute("cart", cart);
                    request.setAttribute("orderInput", orderInput);
                    request.setAttribute("errorMessage", "在庫が不足している物資があります。数量を変更するか別の物資を選択してください。");
                    request.setAttribute("modalAction", "redirect");
                    request.setAttribute("redirectUrl", request.getContextPath() + "/order");
                    request.getRequestDispatcher("/WEB-INF/jsp/order_confirm.jsp").forward(request, response);
                    return;
                }
            }

            for (CartItem ci : cart) {
                itemDao.deductStock(conn, ci.getItemId(), ci.getQuantity());
            }

            String orderNo = OrderNoGenerator.generate();
            Order order = new Order();
            order.setOrderNo(orderNo);
            order.setName(orderInput.getName());
            order.setAddress(orderInput.getAddress());
            order.setPhone(orderInput.getPhone());
            order.setNote(orderInput.getNote());
            order.setDeliveryStatus("未対応");

            int orderId = orderDao.insertOrder(conn, order);

            for (CartItem ci : cart) {
                OrderDetail detail = new OrderDetail();
                detail.setItemId(ci.getItemId());
                detail.setOrderId(orderId);
                detail.setQuantity(ci.getQuantity());
                orderDao.insertOrderDetail(conn, detail);
            }

            conn.commit();

            session.removeAttribute("requestedItems");
            session.removeAttribute("orderInput");
            session.setAttribute("completedOrderNo", orderNo);

            response.sendRedirect(request.getContextPath() + "/order/complete");
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
