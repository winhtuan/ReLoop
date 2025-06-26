package Controller.Paid;

import Model.DAO.auth.UserDao;
import Model.DAO.commerce.OrderDao;
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

    private static final AppConfig config = new AppConfig();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy các tham số từ URL
        String orderCode = request.getParameter("orderCode");  // Lấy orderCode từ URL
        String status = request.getParameter("status");         // Lấy status từ URL (PAID hoặc failure)
        // Kiểm tra xem trạng thái thanh toán có phải là "PAID" không
        if ("PAID".equalsIgnoreCase(status)) {
            // Cập nhật trạng thái đơn hàng là "Paid"
            OrderDao orderDao = new OrderDao();
            String formattedOrderCode = String.format("ORD%04d", Integer.valueOf(orderCode));

            boolean updated = orderDao.updateOrderStatusByOrderId(formattedOrderCode, "paid");

            if (updated) {
                Order order = orderDao.getOrderById(formattedOrderCode);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DAY_OF_MONTH, 30); // +30 ngày
                Date expiryDate = calendar.getTime();
                new UserDao().updatePremiumStatus(order.getUserId(), true, expiryDate);

                // Nếu cập nhật thành công, chuyển hướng đến trang thành công
                response.sendRedirect("home");
            } else {
                // Nếu cập nhật thất bại, hiển thị lỗi
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update order status");
            }
        } else {
            // Nếu thanh toán thất bại, chuyển hướng đến trang hủy
            response.sendRedirect("/ReLoop/html/cancel.html");
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
