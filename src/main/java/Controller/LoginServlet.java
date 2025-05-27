package Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import Model.Account;
import Repository.AccountRep;
import com.google.gson.JsonObject;

public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");
        String state = request.getParameter("state");

        if (code == null) {
            response.getWriter().println("No code found in the request.");
            return;
        }

        try {
            if (state == null || "google".equals(state)) {
                handleGoogleLogin(request, response, code);
            } else if ("facebook".equals(state)) {
                handleFacebookLogin(request, response, code);
            } else {
                response.getWriter().println("Unknown login provider.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in social login", e);
            response.getWriter().println("Error during social login: " + e.getMessage());
        }
    }

    private void handleGoogleLogin(HttpServletRequest request, HttpServletResponse response, String code)
            throws IOException {
        GoogleLogin gg = new GoogleLogin();
        String accessToken = gg.getToken(code);

        if (accessToken == null) {
            response.getWriter().println("Failed to get Google access token.");
            return;
        }

        JsonObject userInfo = gg.getUserInfo(accessToken);
        if (userInfo == null) {
            response.getWriter().println("Failed to get Google user info.");
            return;
        }

        String email = userInfo.get("email").getAsString();
        String name = userInfo.get("name").getAsString();
        processSocialLogin(request, response, email, name);
    }

    private void handleFacebookLogin(HttpServletRequest request, HttpServletResponse response, String code)
            throws IOException {
        FacebookLogin fb = new FacebookLogin();
        String accessToken = fb.getToken(code);

        if (accessToken == null) {
            response.getWriter().println("Failed to get Facebook access token.");
            return;
        }

        JsonObject userInfo = fb.getUserInfo(accessToken);
        if (userInfo == null) {
            response.getWriter().println("Failed to get Facebook user info.");
            return;
        }

        String email = userInfo.has("email") ?
            userInfo.get("email").getAsString() :
            "no-email@" + userInfo.get("id").getAsString() + ".com";
        String name = userInfo.get("name").getAsString();
        processSocialLogin(request, response, email, name);
    }

    private void processSocialLogin(HttpServletRequest request, HttpServletResponse response, String email, String name)
            throws IOException {
        Account acc = new Account(email);

        if (!AccountRep.isEmailExist(email)) {
            request.getSession().setAttribute("user", acc);
            response.sendRedirect("registerGoogle.jsp");
        } else {
            acc = AccountRep.getAccountByEmail(email);
            if (acc != null) {
                request.getSession().setAttribute("user", acc);
                redirectUser(request, response);
            } else {
                response.getWriter().println("Error retrieving account information.");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("remember_me");

        try {
            Account acc = AccountRep.checkLogin(email, password);
            if (acc != null) {
                handleRememberMe(request, response, email, password, rememberMe);
                request.getSession().setAttribute("user", acc);
                redirectUser(request, response);
            } else {
                request.setAttribute("errorMessage", "Please check email, password or verify your account to login!");
                request.getRequestDispatcher("Authenticate/login.jsp").forward(request, response);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database error during login", ex);
            request.setAttribute("errorMessage", "Database error: " + ex.getMessage());
            request.getRequestDispatcher("Authenticate/login.jsp").forward(request, response);
        }
    }

    private void handleRememberMe(HttpServletRequest request, HttpServletResponse response,
            String email, String password, String rememberMe) {
        if ("on".equals(rememberMe)) {
            Cookie emailCookie = new Cookie("userEmail", email);
            emailCookie.setMaxAge(7 * 24 * 60 * 60);
            emailCookie.setPath("/");
            Cookie passCookie = new Cookie("pw", password);
            passCookie.setMaxAge(7 * 24 * 60 * 60);
            passCookie.setPath("/");
            response.addCookie(emailCookie);
            response.addCookie(passCookie);
            LOGGER.info("Remember Me selected - Cookies set: userEmail=" + email);
        } else {
            Cookie emailCookie = new Cookie("userEmail", "");
            emailCookie.setMaxAge(0);
            emailCookie.setPath("/");
            Cookie passCookie = new Cookie("pw", "");
            passCookie.setMaxAge(0);
            passCookie.setPath("/");
            response.addCookie(emailCookie);
            response.addCookie(passCookie);
            LOGGER.info("Remember Me not selected - Cookies cleared");
        }
    }

    private void redirectUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String redirectUrl = (String) request.getSession().getAttribute("redirectUrl");
        if (redirectUrl != null) {
            request.getSession().removeAttribute("redirectUrl");
            response.sendRedirect(redirectUrl);
        } else {
            response.sendRedirect("index.html");
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }
}
