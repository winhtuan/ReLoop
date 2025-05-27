package Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Repository.AccountRep;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import Utils.AppConfig;

@WebServlet("/forgotPassword")
public class ForgotPasswordServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ForgotPasswordServlet.class.getName());
    private static AppConfig config = new AppConfig();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");

        try {
            // Kiểm tra email có tồn tại trong hệ thống
            if (!AccountRep.isEmailExist(email)) {
                request.setAttribute("errorMessage", "Email not found!");
                request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);
                return;
            }

            // Tạo token đặt lại mật khẩu
            String token = UUID.randomUUID().toString();
            long expiryTime = System.currentTimeMillis() + 60 * 60 * 1000; // 1 giờ
            Date expiryDate = new Date(expiryTime);

            // Lưu token reset password vào CSDL
            AccountRep.savePasswordResetToken(email, token, expiryDate);

            // Tạo liên kết reset password
            String resetLink = "http://localhost:8080/ReLoop/resetPassword?token=" + token;

            // Gửi email reset password
            sendResetEmail(email, resetLink);

            request.setAttribute("message", "A password reset link has been sent to your email.");
            request.getRequestDispatcher("Authenticate/forgotPassword.jsp").forward(request, response);

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL error during forgot password", ex);
            request.setAttribute("errorMessage", "Database error: " + ex.getMessage());
            request.getRequestDispatcher("Authenticate/forgotPassword.jsp").forward(request, response);
        } catch (MessagingException ex) {
            LOGGER.log(Level.SEVERE, "Email sending error", ex);
            request.setAttribute("errorMessage", "Failed to send email: " + ex.getMessage());
            request.getRequestDispatcher("Authenticate/forgotPassword.jsp").forward(request, response);
        }
    }

    private void sendResetEmail(String toEmail, String resetLink) throws MessagingException {
        String fromEmail = config.get("email.from"); // Thay bằng email của bạn
        String password = config.get("email.password"); // App Password Gmail

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Password Reset Request");
        message.setText("Click the link below to reset your password:\n\n" + resetLink + "\n\nThis link will expire in 1 hour.");

        Transport.send(message);
    }
}
