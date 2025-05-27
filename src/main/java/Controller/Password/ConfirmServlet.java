package Controller.Password;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Utils.DBUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConfirmServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Lấy token từ URL
        String token = request.getParameter("token");
        if (token == null || token.isEmpty()) {
            printError(out, "Invalid verification link!", "Authenticate/signup.jsp");
            return;
        }

        try (Connection conn = DBUtils.getConnect()) {
            if (conn == null) {
                printError(out, "Unable to connect to the database!", "Authenticate/signup.jsp");
                return;
            }

            // Kiểm tra token
            String checkTokenQuery = "SELECT COUNT(*) FROM Account WHERE verification_token = ? AND is_verified = 0";
            PreparedStatement checkStmt = conn.prepareStatement(checkTokenQuery);
            checkStmt.setString(1, token);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) == 0) {
                printError(out, "Invalid or expired verification link!", "Authenticate/signup.jsp");
                return;
            }

            // Cập nhật trạng thái xác nhận
            String updateQuery = "UPDATE Account SET is_verified = 1, verification_token = NULL WHERE verification_token = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setString(1, token);
            int rows = updateStmt.executeUpdate();

            if (rows > 0) {
                printSuccess(out, "Account Verified Successfully!", "Your account has been activated. Sign in to continue.", "login.jsp");
            } else {
                printError(out, "Failed to verify account!", "Authenticate/signup.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            printError(out, "Error: " + e.getMessage(), "Authenticate/signup.jsp");
        }
    }

    // Phương thức hiển thị thông báo thành công
    private void printSuccess(PrintWriter out, String title, String message, String redirectLink) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Verification Success</title>");
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
        out.println("<a href='" + redirectLink + "'>Sign In Now</a>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
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
}