/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.Admin;

import Model.DAO.admin.AdminPostDAO;
import Utils.AppConfig;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Properties;

/**
 *
 * @author Admin
 */
@WebServlet(name = "ApprovalServlet", urlPatterns = {"/ApprovalServlet"})
public class ApprovalServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ApprovalServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ApprovalServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AdminPostDAO db = new AdminPostDAO();
        String action = request.getParameter("action");
        String productId = request.getParameter("productId");
        String reason = request.getParameter("reason");
        String userId = request.getParameter("userId");
        if (productId != null && !productId.isEmpty()) {
            if ("approve".equals(action)) {
                db.approvePostById(productId);
            } else if ("reject".equals(action)) {
                db.rejectPostById(productId);
                // Giả sử bạn có hàm lấy email người đăng từ productId
                String userEmail = db.getUserEmailByUserId(userId);

                // Gửi email từ chối
                boolean emailSent = sendRejectionEmail(userEmail, reason);

                if (emailSent) {
                    request.setAttribute("message", "Product rejected and email sent successfully.");
                } else {
                    request.setAttribute("error", "Failed to send rejection email.");
                }
            }
         // Sau khi xử lý xong, redirect về trang danh sách chờ duyệt
        response.sendRedirect("ApprovalPost"); // Đổi đường dẫn nếu cần   
        }

    }

    private boolean sendRejectionEmail(String email, String reason) {
        String host = "smtp.gmail.com";
        String from = new AppConfig().get("email.from");
        String pass = new AppConfig().get("email.password");

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

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
            message.setSubject("Your product has been rejected");

            String content = "Dear user,\n\n"
                    + "We regret to inform you that your product listing was rejected for the following reason:\n\n"
                    + reason + "\n\n"
                    + "Please review and consider updating your listing.\n\n"
                    + "Best regards,\nReLoop Team";

            message.setText(content);

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
