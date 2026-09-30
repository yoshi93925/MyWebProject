package servlet;

import dao.ItemDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import model.Item;

public class AdminItemListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ItemDao itemDao = new ItemDao();

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

        try {
            List<Item> items = itemDao.findAll();
            request.setAttribute("items", items);
            request.getRequestDispatcher("/WEB-INF/jsp/admin_items.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
