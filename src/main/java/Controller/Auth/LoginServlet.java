package Controller.Auth;

import Model.DAO.auth.AccountDao;
import Model.DAO.auth.UserDao;
import Model.DAO.commerce.CartDAO;
import Model.entity.auth.Account;
import Model.entity.auth.User;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        GoogleLogin googleLogin = new GoogleLogin();
        String accessToken = googleLogin.getToken(code);

        if (accessToken == null) {
            response.getWriter().println("Failed to get Google access token.");
            return;
        }

        JsonObject userInfo = googleLogin.getUserInfo(accessToken);
        if (userInfo == null) {
            response.getWriter().println("Failed to get Google user info.");
            return;
        }

        String email = userInfo.get("email").getAsString();
        String name = userInfo.get("name").getAsString();
        String picture = userInfo.get("picture").getAsString();

        processSocialLogin(request, response, email, name, picture);
    }

    private void handleFacebookLogin(HttpServletRequest request, HttpServletResponse response, String code)
            throws IOException, ServletException {

        FacebookLogin facebookLogin = new FacebookLogin();
        String accessToken = facebookLogin.getToken(code);

        if (accessToken == null) {
            response.getWriter().println("Failed to get Facebook access token.");
            return;
        }

        JsonObject userInfo = facebookLogin.getUserInfo(accessToken);
        if (userInfo == null) {
            response.getWriter().println("Failed to get Facebook user info.");
            return;
        }

        String email = userInfo.has("email")
                ? userInfo.get("email").getAsString()
                : "no-email@" + userInfo.get("id").getAsString() + ".com";
        String name = userInfo.get("name").getAsString();
        String picture = userInfo.get("picture").getAsString();
        processSocialLogin(request, response, email, name,picture);
        
    }

    private void processSocialLogin(HttpServletRequest request, HttpServletResponse response,
            String email, String name, String pic)
            throws IOException, ServletException {

        Account acc = new Account(email);

        if (!new AccountDao().isEmailExist(email)) {
            request.getSession().setAttribute("user", acc);
            request.getSession().setAttribute("fullname", name);
            request.getSession().setAttribute("picture", pic);
            request.getRequestDispatcher("JSP/Authenticate/registerGoogle.jsp").forward(request, response);
        } else {
            acc = new AccountDao().getAccountByEmail(email);
            if (acc != null) {
                request.getSession().setAttribute("user", acc);
                User user = new UserDao().getUserById(acc.getUserId());
                request.getSession().setAttribute("cus", user);
                redirectUser(request, response);
            } else {
                response.getWriter().println("Error retrieving account information.");
            }
        }
    }
 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String remember = request.getParameter("remember");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Email and password cannot be empty!");
            request.getRequestDispatcher("JSP/Authenticate/JoinIn.jsp").forward(request, response);
            return;
        }

        AccountDao accountDao = new AccountDao();

//    try {
//        Account acc = accountDao.checkLogin(email.trim(), password.trim());
//
//        if (acc != null) {
//            User user = new UserDao().getUserById(acc.getUserId());
//
//            if (user != null) {
//                // Đăng nhập thành công
//                request.getSession().setAttribute("cus", user);
//                request.getSession().setAttribute("user", acc);
//
//                // Kiểm tra role
//                String user_id = acc.getUserId();
//                boolean isAdmin = accountDao.checkIsAdmin(user_id);
//
//                if (isAdmin) {
//                    request.getRequestDispatcher("StatictisServlet").forward(request, response);
//                    return;
//                }
//
//                redirectUser(request, response);
//                return;
//            }
//        }
        try {
            // Kiểm tra đăng nhập
            Account acc = accountDao.checkLogin(email.trim(), password.trim()); // hoặc hashedPassword nếu đã hash
            if (acc != null) {

                if ("on".equals(remember)) {
                    Cookie emailCookie = new Cookie("userEmail", email);
                    Cookie passwordCookie = new Cookie("userPassword", password); // hoặc mã hóa!

                    emailCookie.setMaxAge(7 * 24 * 60 * 60);   // 7 ngày
                    passwordCookie.setMaxAge(7 * 24 * 60 * 60);

                    emailCookie.setPath(request.getContextPath());
                    passwordCookie.setPath(request.getContextPath());

                    response.addCookie(emailCookie);
                    response.addCookie(passwordCookie);
                } else {
                    // Xoá nếu không nhớ nữa
                    Cookie emailCookie = new Cookie("userEmail", "");
                    Cookie passwordCookie = new Cookie("userPassword", "");
                    emailCookie.setMaxAge(0);
                    passwordCookie.setMaxAge(0);
                    response.addCookie(emailCookie);
                    response.addCookie(passwordCookie);
                }

                User user = new UserDao().getUserById(acc.getUserId());
                // Đăng nhập thành công, lưu vào session

                if (user != null) {
                    // Đăng nhập thành công
                    String user_id = acc.getUserId();
                    boolean isAdmin = accountDao.checkIsAdmin(user_id);
                    boolean isuser = accountDao.checkIsUser(user_id);
                    boolean isshopkeeper = accountDao.checkIsshopkeeper(user_id);
                    boolean issuportier = accountDao.checkIssupporter(user_id);
                    if (isAdmin) {
                        request.getRequestDispatcher("StatictisServlet").forward(request, response);
                        return;
                    } 
                    else if (isuser || isshopkeeper) {
                        request.getSession().setAttribute("cus", user);
                        request.getSession().setAttribute("user", acc);
                        int cartN = new CartDAO().getTotalQuantityByUserId(acc.getUserId());
                        request.getSession().setAttribute("cartN", cartN);
                        redirectUser(request, response);
                        return;
                    }
                }

            } else {
                // Đăng nhập thất bại
                request.getSession().setAttribute("Message", "Please check email, password or verify your account to login!");
                redirectUser(request, response);
            }
//            redirectUser(request, response);
            // Nếu acc == null hoặc user == null
//        request.setAttribute("errorMessage", "Please check email, password or verify your account to login!");
//        request.getRequestDispatcher("JSP/Authenticate/JoinIn.jsp").forward(request, response);
        LOGGER.info("Login attempt - Email: " + email + ", Password: " + password);
        LOGGER.info("Test logging email = " + email);

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database error during login", ex);
            request.setAttribute("errorMessage", "Database error: " + ex.getMessage());
            request.getRequestDispatcher("JSP/Authenticate/JoinIn.jsp").forward(request, response);
        }   
    }
    
    private void redirectUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String redirectUrl = (String) request.getSession().getAttribute("redirectUrl");

        // Nếu redirectUrl quay về LoginServlet thì chuyển sang home
        if (redirectUrl != null && !redirectUrl.toLowerCase().contains("loginservlet")) {
            request.getSession().removeAttribute("redirectUrl");
            response.sendRedirect(redirectUrl);
        } else {
            response.sendRedirect("home"); // hoặc servlet mapping khác
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles social logins and standard user logins.";
    }
}
