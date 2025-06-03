package Controller.Password;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Model.DAO.AccountDao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ResetPasswordServlet", urlPatterns = {"/resetPassword"})
public class ResetPasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");

        if (token == null || token.isEmpty()) {
            request.setAttribute("errorMessage", "Invalid or missing token!");
            request.getRequestDispatcher("JSP/Authenticate/resetPassword.jsp").forward(request, response);
            return;
        }

        try {
            // Xác minh token
            String email = AccountDao.verifyResetToken(token);
            if (email == null) {
                request.setAttribute("errorMessage", "Invalid or expired token!");
                request.getRequestDispatcher("JSP/Authenticate/resetPassword.jsp").forward(request, response);
                return;
            }

            // Token hợp lệ, hiển thị form đặt lại mật khẩu
            request.setAttribute("showForm", true);
            request.getRequestDispatcher("JSP/Authenticate/resetPassword.jsp").forward(request, response);

        } catch (SQLException ex) {
            Logger.getLogger(ResetPasswordServlet.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("errorMessage", "Error: " + ex.getMessage());
            request.getRequestDispatcher("JSP/Authenticate/resetPassword.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match!");
            request.setAttribute("showForm", true);
            request.getRequestDispatcher("JSP/Authenticate/resetPassword.jsp").forward(request, response);
            return;
        }

        try {
            // Xác minh token
            String email = AccountDao.verifyResetToken(token);
            if (email == null) {
                request.setAttribute("errorMessage", "Invalid or expired token!");
                request.getRequestDispatcher("JSP/Authenticate/resetPassword.jsp").forward(request, response);
                return;
            }

            // Cập nhật mật khẩu mới
            AccountDao.updatePassword(email, newPassword);

            request.setAttribute("message", "Password reset successfully! Please login with your new password.");
            request.getRequestDispatcher("JSP/Authenticate/resetPassword.jsp").forward(request, response);

        } catch (SQLException ex) {
            Logger.getLogger(ResetPasswordServlet.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("errorMessage", "Error: " + ex.getMessage());
            request.setAttribute("showForm", true);
            request.getRequestDispatcher("JSP/Authenticate/resetPassword.jsp").forward(request, response);
        }
    }
}