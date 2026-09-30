package servlet;

import dao.OrderDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import model.Order;

public class AdminOrderListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String msg = (String) session.getAttribute("successMessage");
            if (msg != null) {
                request.setAttribute("successMessage", msg);
                session.removeAttribute("successMessage");
            }
        }

        String tab = request.getParameter("tab");
        if (tab == null || tab.trim().isEmpty()) {
            tab = "all";
        } else {
            tab = tab.trim().toLowerCase();
        }

        try {
            List<Order> orders = orderDao.findAllWithDetails(tab);
            request.setAttribute("orders", orders);
            request.setAttribute("currentTab", tab);
            request.getRequestDispatcher("/WEB-INF/jsp/admin_orders.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
