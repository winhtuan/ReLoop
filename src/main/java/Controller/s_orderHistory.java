/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Model.DAO.commerce.OrderDao;
import Model.entity.auth.Account;
import Model.entity.commerce.Order;
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
public class s_orderHistory extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Account account = (Account) req.getSession().getAttribute("user");
        List<Order> orders;
        orders = new OrderDao().getOrdersByUserId(account.getUserId());
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("JSP/Commerce/orderHistory.jsp").forward(req, resp);
    }

}
