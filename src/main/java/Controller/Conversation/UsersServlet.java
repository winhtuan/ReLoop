/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.Conversation;


import Model.DAO.AccountDao;
import Model.DAO.CustomerDao;
import Model.Entity.Account;
import Model.Entity.Customer;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import com.google.gson.Gson;

public class UsersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        Account currentUser = (Account) req.getSession().getAttribute("user");
        if (currentUser == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        Account id=(Account)req.getSession().getAttribute("user");
        Customer user = CustomerDao.getCustomerByID(id.getUserId());
        List<Customer> users = CustomerDao.listAllCustomers();
        req.setAttribute("cus", user);
        req.setAttribute("userList", users);
        req.getRequestDispatcher("JSP/Conversation/chatUI.jsp").forward(req, resp);
    }
}

