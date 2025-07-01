/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.Post;

import Model.DAO.post.ProductDao;
import Model.entity.auth.Account;
import Model.entity.post.Product;
import Model.entity.post.ProductAttributeValue;
import Model.entity.post.ProductImage;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Hong Quan
 */
@WebServlet(name = "savePostServlet", urlPatterns = {"/savePostServlet"})
public class savePostServlet extends HttpServlet {

    private static final Gson GSON = new Gson();
    private static final Logger LOGGER = Logger.getLogger(savePostServlet.class.getName());
    private final ProductDao productDAO = new ProductDao();

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
            out.println("<title>Servlet savePostServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet savePostServlet at " + request.getContextPath() + "</h1>");
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
        response.setContentType("application/json"); // Đặt content type là JSON
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("user");
        if (acc == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\":false,\"message\":\"User not logged in\"}");
            return;
        }
        String userId = acc.getUserId(); // Giả sử Account có getUserId()
        LOGGER.info("User ID from session: " + userId);

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        String jsonData = sb.toString();
        LOGGER.info("Received JSON data: " + jsonData);
        Map<String, Object> data = GSON.fromJson(jsonData, Map.class);
        if (data == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"Invalid JSON data\"}");
            return;
        }

        Product product = new Product();
        product.setUserId(userId);
        Object categoryIdObj = data.get("categoryId");
        if (categoryIdObj != null) {
            String categoryIdStr = String.valueOf(categoryIdObj).split("\\.")[0]; // Lấy phần nguyên
            product.setCategoryId(Integer.parseInt(categoryIdStr)); // Chuyển sang Integer
        } else {
            LOGGER.warning("categoryId is null");
        }
        product.setTitle((String) data.get("productTitle"));
        Object priceObj = data.get("productPrice");
        if (priceObj != null) {
            product.setPrice(new BigDecimal(String.valueOf(priceObj))); // Chuyển đổi linh hoạt
        } else {
            LOGGER.warning("productPrice is null");
        }
        product.setDescription((String) data.get("productDescription"));
        product.setLocation((String) data.get("productLocation"));
        product.setState((String) data.get("productState"));
        product.setStatus("active");
        product.setPriority(false);

        List<ProductAttributeValue> attributeValues = new ArrayList<>();
        @SuppressWarnings("unchecked")
        Map<String, String> attributeValuesMap = (Map<String, String>) data.get("attributeValues"); // Sử dụng Map<String, String>
        if (attributeValuesMap != null) {
            for (Map.Entry<String, String> entry : attributeValuesMap.entrySet()) {
                if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                    try {
                        Integer attributeId = Integer.parseInt(entry.getKey()); // Chuyển String key sang Integer
                        ProductAttributeValue attrValue = new ProductAttributeValue();
                        attrValue.setAttributeId(attributeId);
                        attrValue.setValue(entry.getValue());
                        attributeValues.add(attrValue);
                    } catch (NumberFormatException e) {
                        LOGGER.warning("Invalid attributeId: " + entry.getKey());
                    }
                }
            }
        } else {
            LOGGER.warning("attributeValuesMap is null");
        }

        List<ProductImage> images = new ArrayList<>();
        @SuppressWarnings("unchecked")
        List<String> imageUrls = (List<String>) data.get("imageUrls");
        if (imageUrls != null) {
            for (int i = 0; i < imageUrls.size(); i++) {
                ProductImage image = new ProductImage();
                image.setImageUrl(imageUrls.get(i));
                image.setPrimary(i == 0);
                images.add(image);
            }
        } else {
            LOGGER.warning("imageUrls is null");
        }

        try {
            String productId = productDAO.saveProduct(product, attributeValues, images);
            LOGGER.info("Product saved with ID: " + productId);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"success\":true,\"message\":\"Ad posted successfully\",\"productId\":\"" + productId + "\"}");
        } catch (SQLException e) {
            LOGGER.severe("SQL Error: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\":false,\"message\":\"Failed to save product: " + e.getMessage() + "\"}");
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
