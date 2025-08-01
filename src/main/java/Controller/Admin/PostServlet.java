/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.Admin;

import Model.DAO.admin.AdminPostDAO;
import Model.entity.post.Product;
import Model.entity.post.ProductImage;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Admin
 */
@WebServlet(name = "PostServlet", urlPatterns = {"/PostServlet"})
public class PostServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AdminPostDAO ad = new AdminPostDAO();
        List<Product> productList = ad.allPost();

// Duyệt từng product để lấy ảnh
        Map<String, String> imageMap = new HashMap<>();
        for (Product p : productList) {
            List<ProductImage> images = ad.image(p.getProductId());
            if (images != null && !images.isEmpty()) {
                imageMap.put(p.getProductId(), images.get(0).getImageUrl()); // lấy 1 ảnh đầu
            } else {
                imageMap.put(p.getProductId(), "https://via.placeholder.com/60");
            }
        }

        request.setAttribute("approvalPosts", productList);
        request.setAttribute("imageMap", imageMap);
        request.getRequestDispatcher("/JSP/Admin/Post.jsp").forward(request, response);
    }

}
