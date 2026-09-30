package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.CartItem;
import model.Order;

public class OrderAddressServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute("requestedItems");
        if (cart == null || cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/order");
            return;
        }

        Order orderInput = (Order) session.getAttribute("orderInput");
        if (orderInput != null) {
            request.setAttribute("name", orderInput.getName());
            request.setAttribute("address", orderInput.getAddress());
            request.setAttribute("phone", orderInput.getPhone());
            request.setAttribute("note", orderInput.getNote());
        }

        request.getRequestDispatcher("/WEB-INF/jsp/order_address.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute("requestedItems");
        if (cart == null || cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/order");
            return;
        }

        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        String note = request.getParameter("note");

        if (name != null) name = name.trim();
        if (address != null) address = address.trim();
        if (phone != null) phone = phone.trim();
        if (note != null) note = note.trim();

        request.setAttribute("name", name);
        request.setAttribute("address", address);
        request.setAttribute("phone", phone);
        request.setAttribute("note", note);

        if (name == null || name.isEmpty()) {
            request.setAttribute("errorMessage", "お名前を入力してください。");
            request.getRequestDispatcher("/WEB-INF/jsp/order_address.jsp").forward(request, response);
            return;
        }
        if (name.length() > 20) {
            request.setAttribute("errorMessage", "お名前は20文字以内で入力してください。");
            request.getRequestDispatcher("/WEB-INF/jsp/order_address.jsp").forward(request, response);
            return;
        }

        if (address == null || address.isEmpty()) {
            request.setAttribute("errorMessage", "お届け先住所を入力してください。");
            request.getRequestDispatcher("/WEB-INF/jsp/order_address.jsp").forward(request, response);
            return;
        }
        if (address.length() > 255) {
            request.setAttribute("errorMessage", "お届け先住所は255文字以内で入力してください。");
            request.getRequestDispatcher("/WEB-INF/jsp/order_address.jsp").forward(request, response);
            return;
        }

        if (phone == null || phone.isEmpty()) {
            request.setAttribute("errorMessage", "電話番号を入力してください。");
            request.getRequestDispatcher("/WEB-INF/jsp/order_address.jsp").forward(request, response);
            return;
        }
        if (!phone.matches("^[0-9]{1,11}$")) {
            request.setAttribute("errorMessage", "電話番号は11桁以内の半角数字で入力してください。");
            request.getRequestDispatcher("/WEB-INF/jsp/order_address.jsp").forward(request, response);
            return;
        }

        Order orderInput = new Order();
        orderInput.setName(name);
        orderInput.setAddress(address);
        orderInput.setPhone(phone);
        orderInput.setNote(note);

        session.setAttribute("orderInput", orderInput);
        response.sendRedirect(request.getContextPath() + "/order/confirm");
    }
}
