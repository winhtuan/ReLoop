/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.Admin;

import Model.DAO.admin.AdminPostDAO;
import Model.entity.auth.User;
import Model.entity.post.Product;
import Model.entity.post.ProductImage;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Admin
 */
@WebServlet(name = "RejectPost", urlPatterns = {"/RejectPost"})
public class RejectPost extends HttpServlet {

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
            out.println("<title>Servlet RejectPost</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RejectPost at " + request.getContextPath() + "</h1>");
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
         AdminPostDAO ad = new AdminPostDAO();
        List<Product> productList = ad.getRejectPost();

// Duyệt từng product để lấy ảnh
        Map<String, String> imageMap = new HashMap<>();
        for (Product p : productList) {
            List<ProductImage> images = ad.image(p.getProductId());
            if (images != null && !images.isEmpty()) {
                imageMap.put(p.getProductId(), images.get(0).getImageUrl()); // lấy 1 ảnh đầu
            } else {
                imageMap.put(p.getProductId(), "https://via.placeholder.com/60");
            }
        }

        request.setAttribute("rejectedPosts", productList);
        request.setAttribute("imageMap", imageMap);
        HttpSession session = request.getSession(false);
            if(session != null){
                User user = (User) session.getAttribute("cus");
                if(user.getFullName() != null && user.getPhoneNumber() != null && user.getAddress() != null){
                    request.getRequestDispatcher("/JSP/Admin/rejectPost.jsp").forward(request, response);
                }else{
                    request.getRequestDispatcher("s_userProfile").forward(request, response);
                }
            }else{
                request.getRequestDispatcher("/JSP/Admin/JoinIn.jsp").forward(request, response);
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
