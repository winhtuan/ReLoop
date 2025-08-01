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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@WebServlet(name = "savePostServlet", urlPatterns = {"/savePostServlet"})
public class savePostServlet extends HttpServlet {

    private static final Gson GSON = new Gson();
    private static final Logger LOGGER = Logger.getLogger(savePostServlet.class.getName());
    private final ProductDao productDAO = new ProductDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json"); // Đặt content type là JSON
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("user");
        if (acc == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\":false,\"message\":\"User not logged in\"}");
            return;
        }
        String userId = acc.getUserId(); // Giả sử Account có getUserId()

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        String jsonData = sb.toString();
        
        if (jsonData == null || jsonData.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"No data received\"}");
            return;
        }
        
        Map<String, Object> data;
        try {
            data = GSON.fromJson(jsonData, Map.class);
        } catch (Exception e) {
            LOGGER.severe("Failed to parse JSON: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"Invalid JSON format\"}");
            return;
        }
        
        if (data == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"Invalid JSON data\"}");
            return;
        }

        Product product = new Product();
        product.setUserId(userId);
        Object categoryIdObj = data.get("categoryId");
        if (categoryIdObj != null) {
            try {
                String categoryIdStr = String.valueOf(categoryIdObj).split("\\.")[0]; // Lấy phần nguyên
                product.setCategoryId(Integer.parseInt(categoryIdStr)); // Chuyển sang Integer
            } catch (NumberFormatException e) {
                LOGGER.severe("Invalid categoryId format: " + categoryIdObj);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\":false,\"message\":\"Invalid category ID format\"}");
                return;
            }
        } else {
            LOGGER.warning("categoryId is null");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"Category ID is required\"}");
            return;
        }
        String title = (String) data.get("productTitle");
        if (title == null || title.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"Title is required\"}");
            return;
        }
        product.setTitle(title.trim());
        Object priceObj = data.get("productPrice");
        if (priceObj != null) {
            try {
                if (priceObj instanceof Number) {
                    // Nếu là số (Double, Integer), ép kiểu về int
                    product.setPrice(((Number) priceObj).intValue());
                } else if (priceObj instanceof String) {
                    // Nếu là String, parse sang int
                    product.setPrice(Integer.parseInt((String) priceObj));
                } else {
                    LOGGER.warning("productPrice is of unknown type: " + priceObj.getClass().getName());
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"success\":false,\"message\":\"Invalid price format\"}");
                    return;
                }
            } catch (NumberFormatException e) {
                LOGGER.severe("Cannot parse productPrice: " + priceObj);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\":false,\"message\":\"Invalid price format\"}");
                return;
            }
        } else {
            LOGGER.warning("productPrice is null");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"Price is required\"}");
            return;
        }

        String description = (String) data.get("productDescription");
        String location = (String) data.get("productLocation");
        String state = (String) data.get("productState");
        
        if (description == null || description.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"Description is required\"}");
            return;
        }
        if (location == null || location.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"Location is required\"}");
            return;
        }
        if (state == null || state.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"State is required\"}");
            return;
        }
        
        product.setDescription(description.trim());
        product.setLocation(location.trim());
        product.setState(state.trim());
        
        // Set status based on moderation_status
        String moderationStatus = null;
        if (data.containsKey("moderation_status")) {
            moderationStatus = (String) data.get("moderation_status");
        } else if (request.getParameter("moderation_status") != null) {
            moderationStatus = request.getParameter("moderation_status");
        }
        if (moderationStatus == null) {
            moderationStatus = "pending";
        }
        LOGGER.info("Moderation status: " + moderationStatus);
        product.setIsPriority(false);

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
        }

        try {
            LOGGER.info("Moderation status: " + moderationStatus);
            
            // Validate moderation status
            if (moderationStatus != null && !moderationStatus.matches("^(pending|approved|rejected|blocked|warn)$")) {
                LOGGER.warning("Invalid moderation status: " + moderationStatus + ", setting to 'pending'");
                moderationStatus = "pending";
            }
            
            String productId = productDAO.saveProduct(product, attributeValues, images, moderationStatus);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"success\":true,\"message\":\"Ad posted successfully\",\"productId\":\"" + productId + "\"}");
        } catch (SQLException e) {
            LOGGER.severe("SQL Error: " + e.getMessage());
            e.printStackTrace();
            
            // Provide more specific error messages
            String errorMessage = e.getMessage();
            if (errorMessage.contains("moderation_status")) {
                errorMessage = "Invalid moderation status. Please try again.";
            } else if (errorMessage.contains("Data truncated")) {
                errorMessage = "Invalid data format. Please check your input.";
            }
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\":false,\"message\":\"Database error: " + errorMessage + "\"}");
        } catch (Exception e) {
            LOGGER.severe("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\":false,\"message\":\"An unexpected error occurred: " + e.getMessage() + "\"}");
        }
    }

}