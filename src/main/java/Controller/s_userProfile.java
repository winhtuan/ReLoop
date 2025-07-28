/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Model.DAO.commerce.FollowDAO;
import Model.DAO.post.CategoryDAO;
import Model.entity.auth.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@WebServlet(name = "s_userProfile", urlPatterns = {"/s_userProfile"})
public class s_userProfile extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("cus") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("cus");
        FollowDAO followDAO = new FollowDAO();

        try {
            int followingCount = followDAO.countFollowing(user.getUserId());
            int followerCount = followDAO.countFollowers(user.getUserId());
            List<User> listFollowing = followDAO.getFollowingUsers(user.getUserId());
            List<User> listFollower = followDAO.getFollowerUsers(user.getUserId());
            request.setAttribute("following", followingCount);
            request.setAttribute("follower", followerCount);
            request.setAttribute("listfollowing", listFollowing);
            request.setAttribute("listfollower", listFollower);
            request.setAttribute("categoryList", new CategoryDAO().getAllCategories());
            request.setAttribute("user", user);

            if ("admin".equals(user.getRole())) {
                request.getRequestDispatcher("JSP/Admin/profileAdmin.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("JSP/User/userProfile.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // hoặc xử lý riêng nếu cần
    }

    @Override
    public String getServletInfo() {
        return "User Profile Servlet";
    }
}
