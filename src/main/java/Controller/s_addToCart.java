package Controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import Model.DAO.commerce.CartDAO;
import Model.entity.auth.Account;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Thanh Loc
 */
public class s_addToCart extends HttpServlet {

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
            out.println("<title>Servlet s_addToCart</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet s_addToCart at " + request.getContextPath() + "</h1>");
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
        String productId = request.getParameter("postID");
        Account acc = (Account) request.getSession().getAttribute("user");
        String userId = acc.getUserId();
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        CartDAO cartDao = new CartDAO();
        if (!cartDao.hasCart(userId)) {
            cartDao.addNewCart(userId);
        }
        // Kiểm tra xem đã có sản phẩm này trong giỏ chưa
        int existingQuantity = cartDao.getProductQuantityInCart(userId, productId);

        if (existingQuantity > 0) {
            // Nếu đã có → tăng quantity lên 1
            cartDao.updateQuantity(userId, productId, existingQuantity + quantity);
        } else {
            // Nếu chưa có → thêm mới với quantity = 1
            cartDao.addItemToCart(userId, productId, quantity);
        }
        
        int cartN = new CartDAO().getTotalQuantityByUserId(acc.getUserId());
        request.getSession().setAttribute("cartN", cartN);
        request.setAttribute("productId", productId);
        request.setAttribute("messCartAdd", "Add to cart successfully");
        request.getRequestDispatcher("/s_productDetail").forward(request, response);

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
