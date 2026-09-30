package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class OrderCompleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String orderNo = null;
        if (session != null) {
            orderNo = (String) session.getAttribute("completedOrderNo");
            session.removeAttribute("completedOrderNo");
        }

        if (orderNo == null || orderNo.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/top");
            return;
        }

        request.setAttribute("orderNo", orderNo);
        request.getRequestDispatcher("/WEB-INF/jsp/order_complete.jsp").forward(request, response);
    }
}
