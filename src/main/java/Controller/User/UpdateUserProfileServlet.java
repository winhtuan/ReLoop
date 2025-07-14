/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.User;

import Model.DAO.auth.UserDao;
import Model.entity.auth.User;
import Utils.fileHandle.S3Util;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.InputStream;

/**
 *
 * @author ACER
 */
@MultipartConfig
@WebServlet(name = "UpdateUserProfileServlet", urlPatterns = {"/UpdateUserProfileServlet"})
public class UpdateUserProfileServlet extends HttpServlet {

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
            out.println("<title>Servlet UpdateUserProfileServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateUserProfileServlet at " + request.getContextPath() + "</h1>");
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
        processRequest(request, response);
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

         // Lấy session người dùng
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("JoinIn.jsp");
            return;
        }

        User user = (User) session.getAttribute("cus");

        // Lấy dữ liệu từ form
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        // Cập nhật avatar nếu có upload mới
        Part avatarPart = request.getPart("avatar");
        String newAvatarUrl = null;

        if (avatarPart != null && avatarPart.getSize() > 0) {
            String fileName = System.currentTimeMillis() + "_" + avatarPart.getSubmittedFileName();
            InputStream fileContent = avatarPart.getInputStream();
            long fileSize = avatarPart.getSize();
            String contentType = avatarPart.getContentType();

            // Upload lên S3 và lấy URL
            newAvatarUrl = S3Util.uploadFile(fileName, fileContent, fileSize, contentType);
        }

        // Cập nhật thông tin người dùng
        UserDao userDao = new UserDao();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhoneNumber(phone);
        user.setAddress(address);
        if (newAvatarUrl != null) {
            user.setSrcImg(newAvatarUrl);
        }

        userDao.updateUserProfile(user);

        // Cập nhật lại session
        session.setAttribute("user", user);

        // Quay lại trang profile
            response.sendRedirect("s_userProfile");
  
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
