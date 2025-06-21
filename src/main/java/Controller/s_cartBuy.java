/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Model.DAO.auth.UserDao;
import Model.DAO.commerce.CartDAO;
import Model.DAO.commerce.OrderDao;
import Model.DAO.commerce.OrderItemDAO;
import Model.DAO.post.ProductDao;
import Model.entity.auth.Account;
import Model.entity.commerce.Order;
import Model.entity.commerce.OrderItem;
import Model.entity.post.Product;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Thanh Loc
 */
public class s_cartBuy extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet s_cartBuy</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet s_cartBuy at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        List<Order> orders = (List<Order>) request.getSession().getAttribute("pendingOrders");
        if (orders == null || orders.isEmpty()) {
            response.sendRedirect("JSP/Home/cartPage.jsp");
            return;
        }
        Account acc = (Account) request.getSession().getAttribute("user");
        String buyerID = acc.getUserId();
        OrderDao oDao = new OrderDao();
        OrderItemDAO oiDao = new OrderItemDAO();

        for (Order order : orders) {
            int totalPrice = order.getListItems()
                    .stream()
                    .mapToInt(item -> item.getPrice() * item.getQuantity())
                    .sum();
            order.setTotalAmount(totalPrice);
            order.setOrderId(oDao.generateOrderId());
            order.setUserId(buyerID);
            oDao.insert(order); // insert orders
            for (OrderItem item : order.getListItems()) {
                item.setOrderId(order.getOrderId());
                item.setPrice(item.getPrice()*item.getQuantity());
                oiDao.insert(item); // insert order_items
            }
        }

        request.getSession().removeAttribute("pendingOrders");
        response.sendRedirect("JSP/Commerce/checkout_success.jsp");
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
        int orID = 1;
        String buyerId = ((Account) request.getSession().getAttribute("user")).getUserId();
        String[] productIds = request.getParameterValues("productIds");

        if (productIds == null || productIds.length == 0) {
            request.setAttribute("message", "Bạn chưa chọn sản phẩm nào để mua.");
            request.getRequestDispatcher("/JSP/Home/cartPage.jsp").forward(request, response);
            return;
        }

        ProductDao pDao = new ProductDao();
        Map<String, List<OrderItem>> bySeller = new HashMap<>();

        for (String pid : productIds) {
            Product p = pDao.getProductById(pid);
            int qty = Integer.parseInt(request.getParameter("qty_" + pid));

            OrderItem oi = new OrderItem();
            oi.setProductId(pid);
            oi.setQuantity(qty);
            oi.setPrice(p.getPrice());
            oi.setProductName(p.getTitle());
            oi.setSellerName(new UserDao().getUserById(p.getUserId()).getFullName());

            bySeller.computeIfAbsent(p.getUserId(), k -> new ArrayList<>()).add(oi);
        }

        // Tạo danh sách đơn hàng tạm thời
        List<Order> pendingOrders = new ArrayList<>();
        OrderDao oDao = new OrderDao();

        for (Map.Entry<String, List<OrderItem>> entry : bySeller.entrySet()) {
            String sellerId = entry.getKey();
            List<OrderItem> items = entry.getValue();

            int total = items.stream().mapToInt(i -> i.getPrice() * i.getQuantity()).sum();

            Order order = new Order();
            order.setOrderId(orID + "");
            order.setUserId(sellerId);
            order.setTotalAmount(total);
            order.setStatus("PENDING");
            order.setListItems(items);
            orID++;
            pendingOrders.add(order);
        }

        // Lưu danh sách đơn hàng tạm thời vào session
        request.getSession().setAttribute("pendingOrders", pendingOrders);
        response.sendRedirect(request.getContextPath() + "/JSP/Commerce/checkout.jsp");
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
