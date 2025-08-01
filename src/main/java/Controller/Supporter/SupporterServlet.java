/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.Supporter;

import Model.DAO.admin.AdminDAO;
import Model.DAO.admin.AdminPostDAO;
import Model.DAO.auth.UserDao;
import Model.DAO.conversation.ConversationDAO;
import Model.entity.auth.Account;
import Model.entity.auth.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 *
 * @author ACER
 */
@WebServlet(name = "SupporterServlet", urlPatterns = {"/SupporterServlet"})
public class SupporterServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            User cuser = (User) session.getAttribute("cus");
            if (cuser.getFullName() != null && cuser.getPhoneNumber() != null && cuser.getAddress() != null) {
                // Lấy user hiện tại từ session
                Account currentUser = (Account) request.getSession().getAttribute("user");
                if (currentUser == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                AdminPostDAO dao = new AdminPostDAO();
                AdminDAO admin = new AdminDAO();
                int totalUsers = dao.getTotalUsers();
                int totalProducts = dao.getTotalProducts();
                int todayProducts = dao.getTodayTotalProducts();
                double revenue = admin.totalRevenue();

                // Lấy thông tin Customer hiện tại
                User user = new UserDao().getUserById(currentUser.getUserId());

                // Lấy danh sách tất cả Customer đã có cuộc trò chuyện với user
                List<User> users = new UserDao().listAllCustomers(currentUser.getUserId());

                String spID = new ConversationDAO().getSupporterInConversation(currentUser.getUserId());
                users.removeIf(u -> (u.getUserId().equalsIgnoreCase(spID)));
                request.setAttribute("supporterID", spID);

                request.setAttribute("cus", user);
                request.setAttribute("userList", users);

                request.setAttribute("totalUsers", totalUsers);
                request.setAttribute("totalProducts", totalProducts);
                request.setAttribute("todayProducts", todayProducts);
                request.setAttribute("revenue", revenue);
                request.getRequestDispatcher("/JSP/Supporter/supporterDB.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("s_userProfile").forward(request, response);
            }
        } else {
            request.getRequestDispatcher("/JSP/Admin/JoinIn.jsp").forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
