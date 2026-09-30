package servlet;

import dao.OrderDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import model.Order;

public class VolunteerListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String tab = request.getParameter("tab");
        if (tab == null || tab.trim().isEmpty() || "all".equalsIgnoreCase(tab.trim())) {
            tab = "undelivery";
        } else {
            tab = tab.trim().toLowerCase();
        }

        try {
            List<Order> orders = orderDao.findAllWithDetails(tab);
            request.setAttribute("orders", orders);
            request.setAttribute("currentTab", tab);
            request.getRequestDispatcher("/WEB-INF/jsp/volunteer_list.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
