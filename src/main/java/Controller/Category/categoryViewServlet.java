package Controller.Category;

import Model.DAO.post.CategoryDAO;
import Model.DAO.post.ProductDao;
import Model.entity.post.Category;
import Model.entity.post.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "categoryViewServlet", urlPatterns = {"/categoryViewServlet"})
public class categoryViewServlet extends HttpServlet {

    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    CategoryDAO categoryDAO = new CategoryDAO();
    String action = request.getParameter("action");
    if ("getAllCategories".equals(action)) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<Category> allLevelZeroCategories = categoryDAO.getAllLevelZeroCategories();
        Map<String, List<Category>> categoryMap = new HashMap<>();
        for (Category lv0 : allLevelZeroCategories) {
            List<Category> subCategories = categoryDAO.getSubCategoriesByParentId(lv0.getCategoryId());
            categoryMap.put(lv0.getSlug(), subCategories);
        }

        Gson gson = new GsonBuilder().create();
        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("categories", allLevelZeroCategories);
        jsonResponse.put("subCategories", categoryMap);

        PrintWriter out = response.getWriter();
        out.print(gson.toJson(jsonResponse));
        out.flush();
        return;
    }

    // Lấy toàn bộ danh mục để hiển thị trong menu
    List<Category> allCategories = categoryDAO.getAllCategories();
    request.setAttribute("categoryList", allCategories);

    String slug = request.getParameter("slug");
    if (slug != null) {
        slug = URLDecoder.decode(slug, StandardCharsets.UTF_8.toString()); // Decode slug
        System.out.println("Decoded slug: " + slug); // Debug
    } else {
        System.out.println("Slug is null");
    }
    System.out.println("Slug received: " + slug);
    Category category = categoryDAO.getCategoryBySlug(slug);
    if (category == null) {
        System.out.println("Category not found for slug: " + slug);
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
    }

    String minPriceStr = request.getParameter("minPrice");
    String maxPriceStr = request.getParameter("maxPrice");
    String state = request.getParameter("state");

    System.out.println("CategoryDAO initialized");
    ProductDao productDAO = new ProductDao();

    System.out.println("Category found: " + category.getName() + ", ID: " + category.getCategoryId() + ", Level: " + category.getLevel());
    List<Category> categoryListForProducts; // Chỉ dùng để lấy sản phẩm, không phải menu
    List<Integer> categoryIdsForProducts;
    if (category.getLevel() == 0) {
        System.out.println("Processing parent category, fetching self and sub-categories");
        categoryListForProducts = new ArrayList<>();
        categoryListForProducts.add(category);
        List<Integer> subCategoryIds = categoryDAO.getAllSubCategoryIds(category.getCategoryId());
        for (Integer id : subCategoryIds) {
            categoryListForProducts.add(categoryDAO.getCategoryById(id));
        }
        categoryIdsForProducts = subCategoryIds;
    } else {
        System.out.println("Processing sub-category, fetching parent and siblings");
        Integer parentId = category.getParentId();
        if (parentId != null) {
            categoryListForProducts = new ArrayList<>();
            categoryListForProducts.add(categoryDAO.getCategoryById(parentId));
            List<Integer> subCategoryIds = categoryDAO.getAllSubCategoryIds(parentId);
            for (Integer id : subCategoryIds) {
                categoryListForProducts.add(categoryDAO.getCategoryById(id));
            }
            categoryIdsForProducts = Collections.singletonList(category.getCategoryId());
        } else {
            categoryListForProducts = Collections.singletonList(category);
            categoryIdsForProducts = Collections.singletonList(category.getCategoryId());
        }
    }
    System.out.println("Category list for products size: " + categoryListForProducts.size());

    Double minPrice = null;
    Double maxPrice = null;
    try {
        if (minPriceStr != null && !minPriceStr.isEmpty()) {
            minPrice = Double.parseDouble(minPriceStr);
        }
        if (maxPriceStr != null && !maxPriceStr.isEmpty()) {
            maxPrice = Double.parseDouble(maxPriceStr);
        }
    } catch (NumberFormatException e) {
        e.printStackTrace();
    }

    List<Product> productList = productDAO.getProductsByCategoryIdsAndFilter(categoryIdsForProducts, minPrice, maxPrice, state);
    System.out.println("Product list size: " + productList.size());
    request.setAttribute("currentCategory", category);
    request.setAttribute("productList", productList);

    request.getRequestDispatcher("/JSP/Home/category-product.jsp").forward(request, response);
}

}
