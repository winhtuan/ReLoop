package Controller.Category;

import Model.DAO.post.CategoryDAO;
import Model.entity.post.Category;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "allCategoriesServlet", urlPatterns = {"/allCategoriesServlet"})
public class allCategoriesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Category> allCategories = categoryDAO.getAllCategories();

        // Đặt thuộc tính để sử dụng trong JSP
        request.setAttribute("categoryList", allCategories);

        // Forward trực tiếp đến Category.jsp
        request.getRequestDispatcher("/JSP/Home/SearchCategory.jsp").forward(request, response);
    }
}
