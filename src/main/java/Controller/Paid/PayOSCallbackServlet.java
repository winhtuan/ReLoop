package Controller.Paid;

import Model.DAO.auth.UserDao;
import Model.DAO.commerce.OrderDao;
import Model.DAO.pay.PaymentDao;
import Model.entity.commerce.Order;
import Model.entity.commerce.OrderItem;
import Model.entity.pay.Payment;
import Utils.AppConfig;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

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

    private void sendOrderConfirmationEmail(String toEmail, Payment payment, Order order) throws MessagingException {
        String fromEmail = config.get("email.from");
        String password = config.get("email.password");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Order Payment Confirmation");

        StringBuilder content = new StringBuilder();
        content.append("Dear Customer,\n\n");
        content.append("We are pleased to inform you that your order has been paid successfully.\n\n");

        content.append("Order Information:\n");
        content.append("Order ID: ").append(order.getOrderId()).append("\n");
        content.append("Order Date: ").append(order.getCreatedAt()).append("\n\n");

        content.append("📦 Items:\n");
        for (OrderItem item : order.getListItems()) {
            content.append("- ").append(item.getProductName())
                    .append(" x").append(item.getQuantity())
                    .append(" @ ").append(item.getPrice()+"").append(" each\n");
        }

        content.append("\n💳 Payment Details:\n");
        content.append("Payment ID: ").append(payment.getPayId()).append("\n");
        content.append("Amount Paid: $").append(String.format("%.2f", payment.getAmount())).append("\n");
        content.append("Payment Date: ").append(payment.getCreatedAt()).append("\n\n");

        content.append("Thank you for shopping with us!\n");
        content.append("Best regards,\nReLoop Team");

        message.setText(content.toString());

        Transport.send(message);
    }

}
