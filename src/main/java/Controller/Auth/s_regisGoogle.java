package Controller.Auth;

import jakarta.servlet.ServletException;
import java.io.IOException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Model.entity.auth.Account;
import Model.DAO.auth.AccountDao;
import Model.DAO.auth.UserDao;
import Model.DAO.commerce.CartDAO;
import Model.DAO.conversation.ConversationDAO;
import Model.entity.auth.User;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            request.getSession().setAttribute("Message", request.getAttribute("erormess"));
            response.sendRedirect("home");
            return;
        }

        String address = request.getParameter("address");
        String phone = request.getParameter("Phone");

        Account user = (Account) request.getSession().getAttribute("user");
        User newU = new User(new UserDao().generateUserId(), (String) request.getSession().getAttribute("fullname"),
                "user", address, phone, user.getEmail(), false, null, BigDecimal.ONE, (String) request.getSession().getAttribute("picture"));
        request.getSession().setAttribute("cus", newU);
        // Insert new user in users table and get user_id
        String userId = new UserDao().newUser(newU);

        // Add account for this user
        new AccountDao().addAccount(
                user.getEmail(),
                "user",
                userId
        );

        user.setUserId(userId);
        user = new AccountDao().getAccountByEmail(user.getEmail());
        
        //Add supporter for this user
        ConversationDAO condao = new ConversationDAO();
        try {
            condao.createConversation(userId, condao.getLeastBusySupporterId(), "PRD0003");
        } catch (SQLException ex) {
            Logger.getLogger(s_regisGoogle.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        request.getSession().setAttribute("user", user);
        int cartN = new CartDAO().getTotalQuantityByUserId(newU.getUserId());
        request.getSession().setAttribute("cartN", cartN);
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
