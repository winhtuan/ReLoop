package Controller.Auth;

import jakarta.servlet.ServletException;
import java.io.IOException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Model.entity.auth.Account;
import Model.DAO.auth.AccountDao;
import Model.DAO.auth.UserDao;

public class s_regisGoogle extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Typically not used for registration, but could forward to a registration page if needed
        response.sendRedirect("JSP/Authenticate/registerGoogle.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getAttribute("erormess") != null) {
            request.getRequestDispatcher("JSP/Authenticate/registerGoogle.jsp").forward(request, response);
            return;
        }

        String address = request.getParameter("address");
        Account user = (Account) request.getSession().getAttribute("user");

        // Insert new user in users table and get user_id
        String userId = new UserDao().addUser(address, user.getEmail());

        // Add account for this user
        new AccountDao().addAccount(
                user.getEmail(),
                "user",
                userId
        );

        user.setUserId(userId);
        user = new AccountDao().getAccountByEmail(user.getEmail());
        request.getSession().setAttribute("user", user);

        // Redirect logic
        String redirectUrl = (String) request.getSession().getAttribute("redirectUrl");
        if (redirectUrl != null) {
            request.getSession().removeAttribute("redirectUrl");
            response.sendRedirect(redirectUrl);
        } else {
            response.sendRedirect("home");
        }
    }
}
