package Controller;

import Model.DAO.post.ProductDao;
import Model.entity.post.CategoryAttribute;
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
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/EditProductServlet")
public class EditProductServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(EditProductServlet.class.getName());
    private final ProductDao productDao = new ProductDao();
    private static final Gson GSON = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productId = request.getParameter("productId");
        if (productId == null || productId.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/s_manageProduct");
            return;
        }

        Product product = productDao.getProductById(productId);
        if (product == null) {
            response.sendRedirect(request.getContextPath() + "/s_manageProduct");
            return;
        }

        List<ProductAttributeValue> attributeValues = productDao.getAttributeValuesByProductId(productId);
        product.setAttributeValues(attributeValues);

        List<ProductImage> images = productDao.getProductImagesByProductId(productId);

        Integer categoryId = product.getCategoryId();
        List<CategoryAttribute> categoryAttributes = Collections.emptyList();
        if (categoryId != null) {
            categoryAttributes = productDao.getCategoryAttributesByCategoryId(categoryId);
            LOGGER.info("Fetched " + categoryAttributes.size() + " attributes for categoryId: " + categoryId);
        } else {
            LOGGER.warning("categoryId is null for productId: " + productId);
        }

        request.setAttribute("product", product);
        request.setAttribute("attributeValues", attributeValues);
        request.setAttribute("images", images);
        request.setAttribute("categoryAttributes", categoryAttributes);
        request.setAttribute("productJson", GSON.toJson(product)); // Đảm bảo JSON chứa state và quantity

        request.getRequestDispatcher("/JSP/User/editProduct.jsp").forward(request, response);
    }
}
