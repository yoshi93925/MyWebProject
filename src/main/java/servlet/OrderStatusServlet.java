package servlet;

import dao.OrderDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import model.Order;

public class OrderStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String orderNo = request.getParameter("order_no");
        HttpSession session = request.getSession();

        if (orderNo == null || orderNo.trim().isEmpty()) {
            orderNo = (String) session.getAttribute("currentOrderNo");
        } else {
            orderNo = orderNo.trim();
        }

        if (orderNo == null || orderNo.isEmpty()) {
            request.setAttribute("errorMessage", "注文番号がありません");
            request.getRequestDispatcher("/WEB-INF/jsp/top.jsp").forward(request, response);
            return;
        }

        try {
            Order order = orderDao.findByOrderNo(orderNo);
            if (order == null) {
                request.setAttribute("errorMessage", "注文番号がありません");
                request.getRequestDispatcher("/WEB-INF/jsp/top.jsp").forward(request, response);
                return;
            }

            session.setAttribute("currentOrderNo", order.getOrderNo());
            session.setAttribute("currentOrderId", order.getOrderId());
            request.setAttribute("order", order);

            request.getRequestDispatcher("/WEB-INF/jsp/order_status.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
