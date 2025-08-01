package Controller;

import Model.DAO.post.ProductDao;
import Model.entity.post.Product;
import Model.entity.post.ProductAttributeValue;
import Model.entity.post.ProductImage;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/UpdateProductServlet")
public class UpdateProductServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(UpdateProductServlet.class.getName());
    private final ProductDao productDao = new ProductDao();
    private static final Gson GSON = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonData = request.getReader().lines().reduce("", (a, b) -> a + b);
        LOGGER.info("Received JSON data: " + jsonData);
        Product product = GSON.fromJson(jsonData, Product.class);

        if (product == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(GSON.toJson(new ResponseObject(false, "Invalid product data", null)));
            return;
        }

        // Lấy danh sách ảnh và attribute values
        List<ProductImage> images = product.getImages() != null ? product.getImages() : List.of();
        List<ProductAttributeValue> attributeValues = product.getAttributeValues() != null ? product.getAttributeValues() : List.of();

        try {
            String productId = productDao.updateProduct(product, attributeValues, images);
            LOGGER.info("Product updated successfully with productId: " + productId);
            response.getWriter().write(GSON.toJson(new ResponseObject(true, "Product updated successfully", productId)));
        } catch (Exception e) {
            LOGGER.severe("Error updating product: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(GSON.toJson(new ResponseObject(false, "Failed to update product: " + e.getMessage(), null)));
        }
    }

    private static class ResponseObject {
        private boolean success;
        private String message;
        private String productId;

        public ResponseObject(boolean success, String message, String productId) {
            this.success = success;
            this.message = message;
            this.productId = productId;
        }
    }
}