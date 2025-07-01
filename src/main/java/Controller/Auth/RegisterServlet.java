package Controller.Auth;

import Model.entity.auth.Account;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;
import Model.DAO.auth.AccountDao;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import Model.DAO.auth.UserDao;
import Utils.AppConfig;
import Utils.DBUtils;
import java.time.LocalDate;

public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String firstName = request.getParameter("name");
        String lastName = request.getParameter("surname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try (Connection conn = DBUtils.getConnect()) {
            if (conn == null) {
                printError(out, "Unable to connect to the database!", "/ReLoop/home");
                return;
            }

            String checkEmailQuery = "SELECT COUNT(*) FROM Account WHERE email = ?";
            PreparedStatement checkEmailStmt = conn.prepareStatement(checkEmailQuery);
            checkEmailStmt.setString(1, email);
            ResultSet emailRs = checkEmailStmt.executeQuery();
            emailRs.next();
            if (emailRs.getInt(1) > 0) {
                printError(out, "This email is already in use!", "/ReLoop/home?regis=true");
                return;
            }

            String baseUsername = firstName + " " + lastName;
            String username = baseUsername;

            if (new AccountDao().isEmailExist(email)) {
                printError(out, "This email is already in use!", "/ReLoop/home?regis=true");
                return;
            }

            // Hash the password
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // Create verification token
            String token = UUID.randomUUID().toString();

            // Insert new user into users table
            String user_id = new UserDao().addUser(username, email);

            // Generate account ID
            String accId = new AccountDao().generateAccountId();

            // Create Account object with updated constructor
            Account account = new Account(
                    accId,
                    hashedPassword,
                    email,
                    LocalDate.now(),
                    String.valueOf(user_id),
                    token,
                    false
            );

            // Insert Account
            String newAccId = new AccountDao().newAccount(account);
            if (newAccId != null) {
                boolean emailSent = sendConfirmationEmail(email, token);
                if (emailSent) {
                    printSuccess(out, "Registration Successful!", "Please check your email (" + email + ") to verify your account.", "/ReLoop/home");
                } else {
                    printError(out, "Unable to send verification email!", "/ReLoop/home?regis=true");
                }
            } else {
                printError(out, "Registration failed!", "/ReLoop/home?regis=true");
            }

        } catch (Exception e) {
            e.printStackTrace();
            printError(out, "Error: " + e.getMessage(), "/ReLoop/home?regis=true");
        }
    }

    // Phương thức gửi email xác nhận
    private boolean sendConfirmationEmail(String email, String token) {
        String host = "smtp.gmail.com";
        String from = new AppConfig().get("email.from");
        String pass = new AppConfig().get("email.password");
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.debug", "true");
        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(from, pass);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Verify Your Account");
            String confirmationLink = "http://localhost:8080/ReLoop/ConfirmServlet?token=" + token;
            message.setText("Click the link to verify your account: " + confirmationLink);

            Transport.send(message);
            System.out.println("Email sent successfully to " + email);
            return true;
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Phương thức hiển thị thông báo lỗi
    private void printError(PrintWriter out, String message, String redirectLink) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Error</title>");
        out.println("<link href='https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap' rel='stylesheet'>");
        out.println("<style>");
        out.println("body {");
        out.println("    font-family: 'Poppins', sans-serif;");
        out.println("    margin: 0;");
        out.println("    padding: 0;");
        out.println("    height: 100vh;");
        out.println("    display: flex;");
        out.println("    justify-content: center;");
        out.println("    align-items: center;");
        out.println("    background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);");
        out.println("}");
        out.println(".error-container {");
        out.println("    background: #fff;");
        out.println("    padding: 40px;");
        out.println("    border-radius: 15px;");
        out.println("    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);");
        out.println("    text-align: center;");
        out.println("    max-width: 500px;");
        out.println("    width: 90%;");
        out.println("    animation: fadeIn 0.5s ease-in-out;");
        out.println("}");
        out.println("@keyframes fadeIn {");
        out.println("    0% { opacity: 0; transform: scale(0.95); }");
        out.println("    100% { opacity: 1; transform: scale(1); }");
        out.println("}");
        out.println(".error-icon {");
        out.println("    font-size: 50px;");
        out.println("    color: #e63946;");
        out.println("    margin-bottom: 20px;");
        out.println("}");
        out.println("h3 {");
        out.println("    color: #1d3557;");
        out.println("    font-size: 24px;");
        out.println("    font-weight: 600;");
        out.println("    margin-bottom: 15px;");
        out.println("}");
        out.println("p {");
        out.println("    color: #457b9d;");
        out.println("    font-size: 16px;");
        out.println("    margin-bottom: 30px;");
        out.println("}");
        out.println("a {");
        out.println("    display: inline-block;");
        out.println("    padding: 12px 30px;");
        out.println("    background: #e63946;");
        out.println("    color: #fff;");
        out.println("    text-decoration: none;");
        out.println("    border-radius: 25px;");
        out.println("    font-size: 16px;");
        out.println("    font-weight: 600;");
        out.println("    transition: background 0.3s ease, transform 0.2s ease;");
        out.println("}");
        out.println("a:hover {");
        out.println("    background: #d00000;");
        out.println("    transform: translateY(-2px);");
        out.println("}");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='error-container'>");
        out.println("<div class='error-icon'>✖</div>");
        out.println("<h3>Error</h3>");
        out.println("<p>" + message + "</p>");
        out.println("<a href='" + redirectLink + "'>Go Back</a>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    // Phương thức hiển thị thông báo thành công
    private void printSuccess(PrintWriter out, String title, String message, String redirectLink) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Success</title>");
        out.println("<link href='https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap' rel='stylesheet'>");
        out.println("<style>");
        out.println("body {");
        out.println("    font-family: 'Poppins', sans-serif;");
        out.println("    margin: 0;");
        out.println("    padding: 0;");
        out.println("    height: 100vh;");
        out.println("    display: flex;");
        out.println("    justify-content: center;");
        out.println("    align-items: center;");
        out.println("    background: linear-gradient(135deg, #d4fc79 0%, #96e6a1 100%);");
        out.println("}");
        out.println(".success-container {");
        out.println("    background: #fff;");
        out.println("    padding: 40px;");
        out.println("    border-radius: 15px;");
        out.println("    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);");
        out.println("    text-align: center;");
        out.println("    max-width: 500px;");
        out.println("    width: 90%;");
        out.println("    animation: fadeIn 0.5s ease-in-out;");
        out.println("}");
        out.println("@keyframes fadeIn {");
        out.println("    0% { opacity: 0; transform: scale(0.95); }");
        out.println("    100% { opacity: 1; transform: scale(1); }");
        out.println("}");
        out.println(".success-icon {");
        out.println("    font-size: 50px;");
        out.println("    color: #2ecc71;");
        out.println("    margin-bottom: 20px;");
        out.println("}");
        out.println("h3 {");
        out.println("    color: #1d3557;");
        out.println("    font-size: 24px;");
        out.println("    font-weight: 600;");
        out.println("    margin-bottom: 15px;");
        out.println("}");
        out.println("p {");
        out.println("    color: #457b9d;");
        out.println("    font-size: 16px;");
        out.println("    margin-bottom: 30px;");
        out.println("}");
        out.println("a {");
        out.println("    display: inline-block;");
        out.println("    padding: 12px 30px;");
        out.println("    background: #2ecc71;");
        out.println("    color: #fff;");
        out.println("    text-decoration: none;");
        out.println("    border-radius: 25px;");
        out.println("    font-size: 16px;");
        out.println("    font-weight: 600;");
        out.println("    transition: background 0.3s ease, transform 0.2s ease;");
        out.println("}");
        out.println("a:hover {");
        out.println("    background: #27ae60;");
        out.println("    transform: translateY(-2px);");
        out.println("}");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='success-container'>");
        out.println("<div class='success-icon'>✔</div>");
        out.println("<h3>" + title + "</h3>");
        out.println("<p>" + message + "</p>");
        out.println("<a href='" + redirectLink + "'>Sign In</a>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}
