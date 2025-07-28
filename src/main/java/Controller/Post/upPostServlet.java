package Controller.Post;

import Model.DAO.post.CategoryAttributeDAO;
import Model.DAO.post.CategoryDAO;
import Model.DAO.post.CategoryStateOptionsDAO;
import Model.entity.post.Category;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "upPostServlet", urlPatterns = {"/upPostServlet"})
public class upPostServlet extends HttpServlet {

    private CategoryDAO categoryDAO = new CategoryDAO();
    private CategoryStateOptionsDAO categoryStateOptionsDAO = new CategoryStateOptionsDAO();
    private CategoryAttributeDAO categoryAttributeDAO = new CategoryAttributeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8"); // Đảm bảo encoding UTF-8
        request.setCharacterEncoding("UTF-8"); // Đảm bảo request cũng dùng UTF-8
        List<Category> categories = categoryDAO.getAllCategories();
        System.out.println("Fetched categories: " + categories);

        // Tạo cây: Map<parentId, List<Category>>
        Map<Integer, List<Category>> tree = new HashMap<>();
        for (Category cat : categories) {
            Integer parentId = (cat.getParentId() != null) ? cat.getParentId() : -1; // Chuyển null thành -1
            tree.computeIfAbsent(parentId, k -> new ArrayList<>()).add(cat);
        }
        // Convert sang JSON (dùng thư viện Gson)
        System.out.println("Built category tree: " + tree);
        Gson gson = new Gson();
        String categoriesJson = gson.toJson(tree);
        System.out.println("Serialized categoriesJson: " + categoriesJson); // Debug JSON output

        // Lấy categoryAttributes từ DAO
        Map<Integer, List<Map<String, Object>>> categoryAttributes = categoryAttributeDAO.getAllCategoryAttributes();
        System.out.println("CategoryAttributes from DAO: " + (categoryAttributes != null ? categoryAttributes : "null"));
        String categoryAttributesJson = gson.toJson(categoryAttributes != null ? categoryAttributes : new HashMap<>());
        System.out.println("Serialized categoryAttributesJson: " + categoryAttributesJson); // Debug JSON output

        // Lấy categoryStateOptions từ DAO
        Map<Integer, String[]> categoryStateOptions = categoryStateOptionsDAO.getAllCategoryStateOptions();
        System.out.println("CategoryStateOptions from DAO raw: " + (categoryStateOptions != null ? categoryStateOptions : "null"));
        String categoryStateOptionsJson = gson.toJson(categoryStateOptions != null ? categoryStateOptions : new HashMap<>());
        System.out.println("Serialized categoryStateOptionsJson: " + categoryStateOptionsJson); // Debug JSON output
        
        request.setAttribute("categoryList", new CategoryDAO().getAllCategories());
        // Đẩy lên JSP
        request.setAttribute("categoriesJson", categoriesJson);
        request.setAttribute("categoryAttributesJson", categoryAttributesJson);
        request.setAttribute("categoryStateOptionsJson", categoryStateOptionsJson);
        request.getRequestDispatcher("/JSP/UpPost/UpPostPage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
