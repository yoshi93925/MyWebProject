package servlet;

import dao.OrderDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import model.Order;

public class VolunteerDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("order_id");
        String tab = request.getParameter("tab");
        if (tab == null || tab.trim().isEmpty() || "all".equalsIgnoreCase(tab.trim())) {
            tab = "undelivery";
        }
        request.setAttribute("currentTab", tab);

        int orderId;
        try {
            orderId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/volunteer/list?tab=" + tab);
            return;
        }

        try {
            Order order = orderDao.findById(orderId);
            if (order == null) {
                request.setAttribute("errorMessage", "対象の注文は既に削除されています");
                request.setAttribute("modalAction", "redirect");
                request.setAttribute("redirectUrl", request.getContextPath() + "/volunteer/list?tab=" + tab);
                request.setAttribute("orders", orderDao.findAllWithDetails(tab));
                request.getRequestDispatcher("/WEB-INF/jsp/volunteer_list.jsp").forward(request, response);
                return;
            }

            request.setAttribute("order", order);
            request.getRequestDispatcher("/WEB-INF/jsp/volunteer_detail.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
