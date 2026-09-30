package servlet;

import dao.AdminDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import model.Admin;

public class AdminLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final AdminDao adminDao = new AdminDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loginAdmin") != null) {
            response.sendRedirect(request.getContextPath() + "/admin/menu");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/jsp/admin_login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        if (name != null) name = name.trim();
        if (password != null) password = password.trim();

        request.setAttribute("name", name);

        try {
            Admin admin = adminDao.authenticate(name, password);
            if (admin == null) {
                request.setAttribute("errorMessage", "管理者名またはパスワードが違います");
                request.getRequestDispatcher("/WEB-INF/jsp/admin_login.jsp").forward(request, response);
                return;
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("loginAdmin", admin);
            response.sendRedirect(request.getContextPath() + "/admin/menu");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
