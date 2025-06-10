/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.Conversation;


import Model.DAO.AccountDao;
import Model.DAO.ConversationDAO;
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
        List<Customer> users = CustomerDao.listAllCustomers(id.getUserId());
        req.setAttribute("cus", user);
        req.setAttribute("userList", users);
        req.getRequestDispatcher("JSP/Conversation/chatUI.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int sellID=Integer.parseInt(req.getParameter("sellerId"));
        int productID=Integer.parseInt(req.getParameter("productId"));
        Account currentUser = (Account) req.getSession().getAttribute("user");
        if (currentUser == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        Account id=(Account)req.getSession().getAttribute("user");
        Customer user = CustomerDao.getCustomerByID(id.getUserId());
        ConversationDAO.getOrCreateConversation(sellID, user.getCustomerId());
        List<Customer> users = CustomerDao.listAllCustomers(id.getUserId());
        Customer c = users.stream().filter((t)->(t.getCustomerId() == sellID)).findFirst().get();
        users.remove(c);
        users.addFirst(c);
        req.setAttribute("sellid", sellID);
        req.setAttribute("cus", user);
        req.setAttribute("userList", users);
        req.getRequestDispatcher("JSP/Conversation/chatUI.jsp").forward(req, resp);  
    }
    
    
}

