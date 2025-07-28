package Controller;

import Model.DAO.auth.UserDao;
import Model.DAO.commerce.OrderDao;
import Model.DAO.pay.voucherDAO;
import Model.entity.auth.Account;
import Model.entity.auth.User;
import Model.entity.commerce.Order;
import Model.entity.commerce.OrderItem;
import Model.entity.pay.Voucher;
import Service.OrderService;
import Utils.AppConfig;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class s_cartBuy extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Order> orders = (List<Order>) request.getSession().getAttribute("pendingOrders");
        if (orders == null || orders.isEmpty()) {
            response.sendRedirect("JSP/Home/cartPage.jsp");
            return;
        }
        Account acc = (Account) request.getSession().getAttribute("user");
        String buyerId = acc.getUserId();

        // Call service to save orders and their items
        OrderService orderService = new OrderService();
        orderService.saveOrdersWithItems(orders, buyerId);

        // Clean up session and redirect
        request.getSession().removeAttribute("pendingOrders");
        response.sendRedirect("JSP/Commerce/checkout_success.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String buyerId = ((Account) request.getSession().getAttribute("user")).getUserId();
        String[] productIds = request.getParameterValues("productIds");
        User buyer = new UserDao().getUserById(buyerId);

        if (productIds == null || productIds.length == 0) {
            request.setAttribute("message", "You have not selected any products to purchase.");
            request.getRequestDispatcher("/JSP/Home/cartPage.jsp").forward(request, response);
            return;
        }

        // === CALL SERVICE ===
        OrderService orderService = new OrderService();
        Map<String, Integer> productQuantities = orderService.parseProductQuantities(request, productIds);
        Map<String, List<OrderItem>> bySeller = orderService.groupItemsBySeller(productIds, productQuantities);

        String currentMaxOrderId = new OrderDao().generateOrderId(); // e.g. ORD0020
        List<Order> pendingOrders = orderService.createPendingOrders(bySeller, currentMaxOrderId);
        List<Voucher> allVouchers = new OrderDao().getAllVoucher();
        List<Voucher> userVouchers = new voucherDAO().getVouchersByUser(buyerId);
        request.getSession().setAttribute("userVouchers", userVouchers);

        // Save pendingOrders & buyer to session
        request.getSession().setAttribute("goongapi", new AppConfig().get("map.apikey"));
        request.getSession().setAttribute("pendingOrders", pendingOrders);
        request.getSession().setAttribute("allVouchers", allVouchers);
        request.getSession().setAttribute("buyer", buyer);
        response.sendRedirect(request.getContextPath() + "/JSP/Commerce/checkout.jsp");
    }

}
