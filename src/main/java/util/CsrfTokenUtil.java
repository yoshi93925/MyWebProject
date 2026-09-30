package util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;

public class CsrfTokenUtil {
    private static final String TOKEN_ATTR = "csrfToken";

    public static String generateToken(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String token = UUID.randomUUID().toString();
        session.setAttribute(TOKEN_ATTR, token);
        return token;
    }

    public static boolean validateToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        String sessionToken = (String) session.getAttribute(TOKEN_ATTR);
        String requestToken = request.getParameter(TOKEN_ATTR);
        if (sessionToken != null && sessionToken.equals(requestToken)) {
            session.removeAttribute(TOKEN_ATTR);
            return true;
        }
        return false;
    }
}
