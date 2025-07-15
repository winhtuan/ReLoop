/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Model.DAO.commerce.favoriteDAO;
import Model.entity.post.Product;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thanh Loc
 */
public class s_favorite extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        favoriteDAO favoriteDAO = new favoriteDAO();
        List<Product> favoriteList = favoriteDAO.getFavoriteProducts(userId);

        request.setAttribute("favorites", favoriteList);
        request.getRequestDispatcher("JSP/Commerce/Favorite.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userId = request.getParameter("userId");
        String productId = request.getParameter("productId");
        String state = request.getParameter("state");

        favoriteDAO favoriteDAO = new favoriteDAO();
        boolean isFavorited;
        try {
            isFavorited = favoriteDAO.isFavorited(userId, productId);
            if (isFavorited) {
                favoriteDAO.removeFavorite(userId, productId);
                request.getSession().setAttribute("Message", "Remove product from favorite list successfull.");
                if (state != null) {
                    response.sendRedirect("s_favorite?userId="+userId);
                } else {
                    response.sendRedirect("NewPostPage");
                }
            } else {
                favoriteDAO.addFavorite(userId, productId);
                request.getSession().setAttribute("Message", "Add to favorite list successfull.");
                if (state != null) {
                    response.sendRedirect("s_favorite?userId="+userId);
                } else {
                    response.sendRedirect("NewPostPage");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(s_favorite.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
