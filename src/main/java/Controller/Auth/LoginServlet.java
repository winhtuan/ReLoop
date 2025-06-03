package Controller.Auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import Model.Entity.Account;
import Model.DAO.AccountDao;
import Utils.AppConfig;
import com.google.gson.JsonObject;
import java.util.HashMap;

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
            throws IOException, ServletException {
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
            throws IOException, ServletException {
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

        String email = userInfo.has("email")
                ? userInfo.get("email").getAsString()
                : "no-email@" + userInfo.get("id").getAsString() + ".com";
        String name = userInfo.get("name").getAsString();
        processSocialLogin(request, response, email, name);
    }

    private void processSocialLogin(HttpServletRequest request, HttpServletResponse response, String email, String name)
            throws IOException, ServletException {
        Account acc = new Account(email);

        if (!AccountDao.isEmailExist(email)) {
            request.getSession().setAttribute("user", acc);
            request.getRequestDispatcher("JSP/Authenticate/registerGoogle.jsp").forward(request, response);
        } else {
            acc = AccountDao.getAccountByEmail(email);
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
        try {
            Account acc = AccountDao.checkLogin(email, password);
            if (acc != null) {
                request.getSession().setAttribute("user", acc);
                request.getRequestDispatcher("JSP/Home/HomePage.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Please check email, password or verify your account to login!");
                request.getRequestDispatcher("JSP/Authenticate/JoinIn.jsp").forward(request, response);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database error during login", ex);
            request.setAttribute("errorMessage", "Database error: " + ex.getMessage());
            request.getRequestDispatcher("JSP/Authenticate/JoinIn.jsp").forward(request, response);
        }
    }

    private void redirectUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String redirectUrl = (String) request.getSession().getAttribute("redirectUrl");
        if (redirectUrl != null) {
            request.getSession().removeAttribute("redirectUrl");
            response.sendRedirect(redirectUrl);
        } else {
            request.getRequestDispatcher("JSP/Home/HomePage.jsp").forward(request, response);
        }
    }
}
