package servlet;

import dao.ItemDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.CartItem;
import model.Item;

public class OrderItemsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ItemDao itemDao = new ItemDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Item> items = itemDao.findAvailable();
            request.setAttribute("items", items);

            HttpSession session = request.getSession();
            @SuppressWarnings("unchecked")
            List<CartItem> cart = (List<CartItem>) session.getAttribute("requestedItems");
            Map<Integer, Integer> selectedQtyMap = new HashMap<>();
            if (cart != null) {
                for (CartItem ci : cart) {
                    selectedQtyMap.put(ci.getItemId(), ci.getQuantity());
                }
            }
            request.setAttribute("selectedQtyMap", selectedQtyMap);

            request.getRequestDispatcher("/WEB-INF/jsp/order_items.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Item> items = itemDao.findAvailable();
            List<CartItem> cart = new ArrayList<>();
            Map<Integer, Integer> selectedQtyMap = new HashMap<>();

            for (Item item : items) {
                String qtyParam = request.getParameter("qty_" + item.getItemId());
                int qty = 0;
                if (qtyParam != null && !qtyParam.trim().isEmpty()) {
                    try {
                        qty = Integer.parseInt(qtyParam.trim());
                    } catch (NumberFormatException ignored) {
                    }
                }
                if (qty > 0) {
                    if (qty > item.getStock()) {
                        qty = item.getStock();
                    }
                    cart.add(new CartItem(
                        item.getItemId(),
                        item.getItemName(),
                        item.getLocation(),
                        item.getGenreId(),
                        qty,
                        item.getStock()
                    ));
                    selectedQtyMap.put(item.getItemId(), qty);
                }
            }

            if (cart.isEmpty()) {
                request.setAttribute("items", items);
                request.setAttribute("selectedQtyMap", selectedQtyMap);
                request.setAttribute("errorMessage", "物資を1つ以上選択してください。");
                request.getRequestDispatcher("/WEB-INF/jsp/order_items.jsp").forward(request, response);
                return;
            }

            HttpSession session = request.getSession();
            session.setAttribute("requestedItems", cart);
            response.sendRedirect(request.getContextPath() + "/order/address");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
