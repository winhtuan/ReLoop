package Controller.Paid;

import Model.DAO.auth.UserDao;
import Model.DAO.commerce.OrderDao;
import Model.DAO.pay.PaymentDao;
import Model.entity.commerce.Order;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;

public class PayOSCallbackServlet extends HttpServlet {

    public static final String ORDER_STATUS_SHIPPED = "shipping";
    public static final String ORDER_STATUS_CANCELLED = "cancelled";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderCode = request.getParameter("orderCode");
        String status = request.getParameter("status");
        response.setContentType("text/html; charset=UTF-8");

        try {
            OrderDao orderDao = new OrderDao();
            PaymentDao paymentDao = new PaymentDao();

            if (orderCode == null || !orderCode.matches("\\d+")) {
                response.getWriter().write("Invalid order code!");
                return;
            }

            String formattedOrderCode = String.format("ORD%04d", Integer.valueOf(orderCode));
            String paymentId = paymentDao.getPaymentIdByOrderId(formattedOrderCode);

            if ("PAID".equalsIgnoreCase(status)) {
                if (paymentId == null) {
                    response.getWriter().write("No payment found for order: " + formattedOrderCode);
                    return;
                }

                // 1. Update payment status first
                boolean paymentUpdated = paymentDao.updatePaymentStatus(paymentId, "paid");
                if (!paymentUpdated) {
                    response.getWriter().write("Failed to update payment status: " + paymentId);
                    return;
                }

                // 2. Only update order status if payment is updated successfully
                Order order = orderDao.getOrderById(formattedOrderCode);
                boolean orderUpdated = false;

                if (order != null && order.getVoucherId() == null && order.getShippingAddress() == null) {
                    // Premium upgrade order
                    orderUpdated = orderDao.updateOrderStatusByOrderId(formattedOrderCode, "paid");
                    // Upgrade user's premium status
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.DAY_OF_MONTH, 30);
                    Date expiryDate = calendar.getTime();
                    new UserDao().updatePremiumStatus(order.getUserId(), true, expiryDate);
                } else {
                    // Normal order: set status to "shipping"
                    orderUpdated = orderDao.updateOrderStatusByOrderId(formattedOrderCode, ORDER_STATUS_SHIPPED);
                }

                if (!orderUpdated) {
                    response.getWriter().write("Failed to update order status: " + formattedOrderCode);
                    return;
                }

                // Success
                response.sendRedirect(request.getContextPath() + "/home?userid=" + order.getUserId());

            } else {
                // Cancelled or failed payment
                if (paymentId != null) {
                    paymentDao.updatePaymentStatus(paymentId, "failed");
                }
                orderDao.updateOrderStatusByOrderId(formattedOrderCode, ORDER_STATUS_CANCELLED);
                response.sendRedirect(request.getContextPath() + "/html/cancel.html");
            }
        } catch (Exception e) {
            response.getWriter().write("System error: " + e.getMessage());
        }
    }
}
