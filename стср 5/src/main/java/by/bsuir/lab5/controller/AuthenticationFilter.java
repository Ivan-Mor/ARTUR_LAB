package by.bsuir.lab5.controller;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

/** Authorizes controller requests and checks the CSRF token for every POST. */
public class AuthenticationFilter implements Filter {
    private static String token() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
    @Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest r = (HttpServletRequest) request;
        HttpServletResponse s = (HttpServletResponse) response;
        r.setCharacterEncoding("UTF-8");
        String action = Objects.toString(r.getParameter("action"), "projects");
        boolean publicAction = action.equals("login") || action.equals("register");
        HttpSession session = r.getSession(false);
        boolean authenticated = session != null && session.getAttribute("username") != null;
        if (!authenticated && !publicAction) {
            if ("GET".equals(r.getMethod())) s.sendRedirect(r.getContextPath() + "/controller?action=login");
            else s.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (publicAction && session == null) session = r.getSession(true);
        if (session != null) {
            synchronized (session) { if (session.getAttribute("csrf") == null) session.setAttribute("csrf", token()); }
        }
        if ("POST".equals(r.getMethod())) {
            String supplied = r.getParameter("csrf");
            String expected = session == null ? null : (String) session.getAttribute("csrf");
            if (supplied == null || expected == null || !MessageDigest.isEqual(expected.getBytes(StandardCharsets.UTF_8), supplied.getBytes(StandardCharsets.UTF_8))) {
                s.sendError(HttpServletResponse.SC_FORBIDDEN); return;
            }
        }
        if ((action.equals("edit") || action.equals("save") || action.equals("delete")) &&
                (!authenticated || !"ADMIN".equals(session.getAttribute("role")))) {
            s.sendError(HttpServletResponse.SC_FORBIDDEN); return;
        }
        chain.doFilter(request, response);
    }
}
