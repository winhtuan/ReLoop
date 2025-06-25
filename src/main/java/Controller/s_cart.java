/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Model.DAO.commerce.CartDAO;
import Model.entity.auth.Account;
import Model.entity.post.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.mail.Session;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.util.List;

/**
 *
 * @author Thanh Loc
 */
public class s_cart extends HttpServlet {

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
            out.println("<title>Servlet s_cart</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet s_cart at " + request.getContextPath() + "</h1>");
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
        HttpSession se = request.getSession();
        Account ac = (Account) se.getAttribute("user");
        List<Product> ls = new CartDAO().getCartProductsByUserId(ac.getUserId());
        se.setAttribute("cartItems", ls);
        request.getRequestDispatcher("JSP/Home/cartPage.jsp").forward(request, response);
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
        StringBuilder json = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
        }

        Gson gson = new Gson();
        List<CartUpdate> updates = gson.fromJson(json.toString(), new TypeToken<List<CartUpdate>>() {
        }.getType());
        if (updates == null) {
            return;
        }
        // Lấy userId từ session
        HttpSession session = request.getSession(false);
        Account ac = (Account) session.getAttribute("user");
        String userId = ac.getUserId();
        if (userId != null) {
            CartDAO dao = new CartDAO();
            for (CartUpdate u : updates) {
                dao.updateQuantity(userId, u.getProductId(), u.getQuantity());
            }
        }
        Account acc=(Account) request.getSession().getAttribute("user");
        int cartN = new CartDAO().getTotalQuantityByUserId(acc.getUserId());
        request.getSession().setAttribute("cartN", cartN);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    static class CartUpdate {

        private String productId;
        private int quantity;

        public String getProductId() {
            return productId;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productId = request.getParameter("productId");
        Account ac = (Account) request.getSession().getAttribute("user");
        String userId = ac.getUserId();
        if (userId != null && productId != null) {
            new CartDAO().removeItemFromCart(userId, productId);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
