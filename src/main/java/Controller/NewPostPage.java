package Controller;

import Model.DAO.post.CategoryDAO;
import Model.DAO.post.ProductDao;
import Model.entity.post.Category;
import Model.entity.post.Product;
import Service.NewPost;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public class NewPostPage extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> allProducts = new ProductDao().getAllProducts();
        allProducts = new NewPost().filterPriorityPost(allProducts);
        request.getSession().setAttribute("allPost", allProducts);
        CategoryDAO categoryDAO = new CategoryDAO();
        List<Category> categoryList = categoryDAO.getAllCategories();
        
        request.setAttribute("categoryList", categoryList);
        request.getRequestDispatcher("JSP/Post/NewPost.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

}

