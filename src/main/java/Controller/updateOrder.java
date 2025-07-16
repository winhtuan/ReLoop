package Controller;

import Model.DAO.commerce.OrderDao;
import Model.entity.auth.Account;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet to handle order status updates (cancel, return, received).
 */
public class updateOrder extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(updateOrder.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Account account = (Account) req.getSession().getAttribute("user");
        String action = req.getParameter("action");
        String orderIdStr = req.getParameter("orderId");
        OrderDao orderDao = new OrderDao();

        if (orderIdStr != null && account != null) {
            try {
                boolean success = false;
                String message = "Unknown action.";

                // Lấy trạng thái hiện tại
                String currentStatus = orderDao.getOrderStatusByOrderId(orderIdStr);
                LOGGER.info("Current status for order " + orderIdStr + " is: " + currentStatus);

                if ("cancel".equalsIgnoreCase(action)) {
                    if (!"pending".equals(currentStatus)) {
                        message = "Order " + orderIdStr + " cannot be cancelled (invalid status: " + currentStatus + ").";
                    } else {
                        success = orderDao.updateOrderStatusByOrderId(orderIdStr, "cancelled");
                        message = success ? "Order " + orderIdStr + " has been cancelled." : "Failed to cancel order " + orderIdStr + ".";
                    }
                } else if ("received".equalsIgnoreCase(action)) {
                    if ("pending".equals(currentStatus)) {
                        message = "Awaiting confirmation.";
                    } else if ("paid".equals(currentStatus)) {
                        message = "Order is being prepared.";
                    } else if (!"shipping".equals(currentStatus) && !"delivered".equals(currentStatus)) {
                        message = "Order " + orderIdStr + " cannot be marked as received (invalid status: " + currentStatus + ").";
                    } else {
                        success = orderDao.updateOrderStatusByOrderId(orderIdStr, "received");
                        message = success ? "Order " + orderIdStr + " has been marked as received." : "Failed to confirm receipt for order " + orderIdStr + ".";
                    }
                } else if ("return".equalsIgnoreCase(action)) {
                    if (!"delivered".equals(currentStatus) && !"received".equals(currentStatus)) {
                        message = "Order " + orderIdStr + " cannot be returned (invalid status: " + currentStatus + ").";
                    } else {
                        success = orderDao.updateOrderStatusByOrderId(orderIdStr, "refunded");
                        message = success ? "Order " + orderIdStr + " has been returned." : "Failed to return order " + orderIdStr + ".";
                    }
                }

                // Luôn trả JSON
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                PrintWriter out = resp.getWriter();
                out.print("{\"success\": " + success + ", \"message\": \"" + message.replace("\"", "'") + "\"}");
                out.flush();

            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Database error while updating order " + orderIdStr, e);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                PrintWriter out = resp.getWriter();
                out.print("{\"success\": false, \"message\": \"Database error: " + e.getMessage().replace("\"", "'") + "\"}");
                out.flush();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Unexpected error while updating order " + orderIdStr, e);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                PrintWriter out = resp.getWriter();
                out.print("{\"success\": false, \"message\": \"An unexpected error occurred: " + e.getMessage().replace("\"", "'") + "\"}");
                out.flush();
            }
        } else {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter out = resp.getWriter();
            out.print("{\"success\": false, \"message\": \"User or order ID is missing.\"}");
            out.flush();
        }
    }
}