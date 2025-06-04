/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Model.DAO.ProductDao;
import Model.Entity.Product;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author phuc2
 */
public class s_search extends HttpServlet {

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
            out.println("<title>Servlet s_search</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet s_search at " + request.getContextPath() + "</h1>");
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String keyword = request.getParameter("query");
        List<Product> productList = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            response.getWriter().print("[]");
            return;
        }
        try {
            productList = ProductDao.getProductSearch(keyword);
        } catch (SQLException ex) {
            Logger.getLogger(s_search.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<Object> simpleProducts = new ArrayList<>();
        for (Product p : productList) {
            String firstImage = (p.getImages() != null && !p.getImages().isEmpty())
                    ? p.getImages().get(0).getImageUrl()
                    : "default.jpg";
            simpleProducts.add(new Object() {
                public final int id = p.getId();
                public final String title = p.getTitle();
                public final Object price = p.getPrice();
                public final String imageUrl = firstImage;
            });
        }
        for(Object a:simpleProducts)
        {
            System.out.println(a.toString());
        }
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(productList));
        out.flush();
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
