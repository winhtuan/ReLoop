package Controller.User;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import Model.DAO.auth.UserDao;
import Model.DAO.commerce.FollowDAO;
import Model.DAO.post.ProductDao;
import Model.entity.auth.User;
import Model.entity.post.Product;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 *
 * @author ACER
 */
@WebServlet(urlPatterns = {"/ListProductServlet"})
public class ListProductServlet extends HttpServlet {

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
            out.println("<title>Servlet ListProductServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ListProductServlet at " + request.getContextPath() + "</h1>");
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
    HttpSession session = request.getSession(false);
    User user = (User) session.getAttribute("cus");

    if (user == null) {
        response.sendRedirect("JoinIn.jsp");
        return;
    }

    String userId = request.getParameter("userId");

    ProductDao productDao = new ProductDao();
    List<Product> listProduct = productDao.getProductsByUserId(userId);

    int follow = 0;
    try {
        FollowDAO followDAO = new FollowDAO();
        follow = followDAO.countFollowers(userId);  // ✅ Bọc try-catch cho phần này
    } catch (Exception e) {
        e.printStackTrace(); // hoặc log lỗi bằng Logger
        follow = 0; // fallback nếu lỗi
    }

    UserDao userDao = new UserDao();
    User u = userDao.getUserById(userId);

    request.setAttribute("user", u);
    request.setAttribute("follower", follow);
    request.setAttribute("followingProducts", listProduct);
    request.setAttribute("followingProductsJson", new com.google.gson.Gson().toJson(listProduct));

    request.getRequestDispatcher("/JSP/User/followingProduct.jsp").forward(request, response);
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
        processRequest(request, response);
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
