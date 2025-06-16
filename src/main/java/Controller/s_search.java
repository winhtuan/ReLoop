package Controller;

import Model.DAO.post.ProductDao;
import Model.entity.post.Product;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;

public class s_search extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("query");
        if (keyword == null || keyword.trim().isEmpty()) {
            response.getWriter().print("[]");
            return;
        }

        // Directly get product list
        List<Product> productList = new ProductDao().searchProducts(keyword);
        // Create a simpler version of products for the response
        List<Map<String, Object>> simpleProducts = new ArrayList<>();
        for (Product p : productList) {
            Map<String, Object> simpleProduct = new HashMap<>();
            simpleProduct.put("product_id", p.getProductId());
            simpleProduct.put("title", p.getTitle());
            simpleProduct.put("price", p.getPrice());
            // No images loaded here; you can add them later with a join or additional call
            simpleProduct.put("imageUrl", p.getImages().get(0).getImageUrl());
            simpleProducts.add(simpleProduct);
        }
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(simpleProducts));
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = (String) request.getParameter("search");
        List<Product> productList = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            response.getWriter().print("[key word empty]");
            return;
        }
        productList = new ProductDao().searchProducts(keyword);
        request.getSession().setAttribute("listProduct", productList);
        request.getRequestDispatcher("JSP/Home/listProduct.jsp").forward(request, response);
        //response.sendRedirect("JSP/Home/shop.html");
    }

    @Override
    public String getServletInfo() {
        return "Product search servlet returning simplified JSON products";
    }

}
