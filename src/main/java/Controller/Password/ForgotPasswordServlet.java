package Controller.Password;

import Model.DAO.auth.AccountDao;
import Utils.AppConfig;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Date;

@WebServlet("/forgotPassword")
public class ForgotPasswordServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ForgotPasswordServlet.class.getName());
    private static final AppConfig config = new AppConfig();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        try {
            // Check if email exists
            if (!new AccountDao().isEmailExist(email)) {
                request.setAttribute("errorMessage", "Email not found!");
                request.getRequestDispatcher("JSP/Authenticate/forgotPassword.jsp").forward(request, response);
                return;
            }

            // Generate reset token & expiry
            String token = UUID.randomUUID().toString();
            long expiryMillis = System.currentTimeMillis() + 60 * 60 * 1000; // 1 hour
            Date expiryDate = new Date(expiryMillis);

            // Save token to DB
            new AccountDao().savePasswordResetToken(email, token, expiryDate);

            // Build reset link
            String resetLink = "http://localhost:8080/ReLoop/resetPassword?token=" + token;

            // Send reset email
            sendResetEmail(email, resetLink);

            request.setAttribute("message", "A password reset link has been sent to your email.");
            request.getRequestDispatcher("JSP/Authenticate/forgotPassword.jsp").forward(request, response);

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database error during password reset", ex);
            request.setAttribute("errorMessage", "Database error: " + ex.getMessage());
            request.getRequestDispatcher("JSP/Authenticate/forgotPassword.jsp").forward(request, response);
        } catch (MessagingException ex) {
            LOGGER.log(Level.SEVERE, "Email sending error", ex);
            request.setAttribute("errorMessage", "Failed to send email: " + ex.getMessage());
            request.getRequestDispatcher("JSP/Authenticate/forgotPassword.jsp").forward(request, response);
        }
    }

    private void sendResetEmail(String toEmail, String resetLink) throws MessagingException {
        String fromEmail = config.get("email.from");
        String password = config.get("email.password");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Password Reset Request");
        message.setText("Click the link below to reset your password:\n\n" + resetLink +
                "\n\nThis link will expire in 1 hour.");

        Transport.send(message);
    }
}
