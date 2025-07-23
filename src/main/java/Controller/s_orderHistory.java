package Controller;

import Model.DAO.commerce.OrderDao;
import Model.DAO.post.CategoryDAO;
import Model.entity.auth.Account;
import Model.entity.commerce.Order;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public class s_orderHistory extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Account account = (Account) req.getSession().getAttribute("user");
        List<Order> orders;
        orders = new OrderDao().getOrdersByUserId(account.getUserId());
        req.setAttribute("orders", orders);
        req.setAttribute("categoryList", new CategoryDAO().getAllCategories());
        req.getRequestDispatcher("JSP/Commerce/orderHistory.jsp").forward(req, resp);
    }

}
