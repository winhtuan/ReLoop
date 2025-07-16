package Controller;

import Model.DAO.post.ProductDao;
import Model.entity.post.Product;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class s_productDetail extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("productId");
        if (id == null) {
            id = (String) request.getAttribute("productId");
        }
        Product p = null;
        p = new ProductDao().getProductById(id);
        request.getSession().setAttribute("product", p);

        // Gọi servlet s_Feedback để lấy feedback
        request.setAttribute("productId", id);
        request.getRequestDispatcher("/s_Feedback").include(request, response);

        // Forward trực tiếp sau khi include
        request.getRequestDispatcher("JSP/Home/productDetail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("productId");
        if (id == null) {
            id = (String) request.getAttribute("productId");
        }
        Product p = null;
        p = new ProductDao().getProductById(id);
        request.getSession().setAttribute("product", p);

        // Gọi servlet s_Feedback để lấy feedback
        request.setAttribute("productId", id);
        request.getRequestDispatcher("/s_Feedback").include(request, response);

        request.getRequestDispatcher("JSP/Home/productDetail.jsp").forward(request, response);
    }
}