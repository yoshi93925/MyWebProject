package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class AdminAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = uri.substring(contextPath.length());

        if (path.equals("/admin/login")) {
            chain.doFilter(req, res);
            return;
        }

        if (path.startsWith("/admin/")) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("loginAdmin") == null) {
                response.sendRedirect(contextPath + "/admin/login");
                return;
            }
        }

        chain.doFilter(req, res);
    }
}
