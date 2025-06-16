package Controller.Paid;

import Model.DAO.commerce.OrderDao;
import Model.DAO.pay.PaidServiceDAO;
import Model.entity.pay.PaidService;
import Utils.AppConfig;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentData;

public class CreateQRPaymentServlet extends HttpServlet {

    private PayOS payOS = new PayOS(new AppConfig().get("payos.client_id"),
             new AppConfig().get("payos.api_key"), new AppConfig().get("payos.checksum_key"));

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("paidService_id");
        String user_id = request.getParameter("user_id");

        // Retrieve the paid service details
        PaidService premium = new PaidServiceDAO().getPaidServiceById(id);
        String description = String.format("purchases %s", premium.getServiceName());
        int amount = (int) premium.getPrice();

        // Generate orderID using the OrderDao method
        String orderId = new OrderDao().generateOrderId();  // Generate unique orderID

        // Initial order status
        String status = "pending"; 

        // Create order in the database
        boolean created = new OrderDao().createOrder(orderId, user_id, amount, status, null, null, null, 0);
        if (!created) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create order");
            return;
        }

        // Prepare payment data to be sent to PayOS
        PaymentData paymentData = PaymentData.builder()
                .orderCode(Long.valueOf(orderId.substring(3)))  // Pass orderId as orderCode for PayOS
                .amount(amount)
                .description(description)
                .returnUrl("http://localhost:8080/ReLoop/PayOSCallback")  // Pass orderId in the return URL
                .cancelUrl("http://localhost:8080/ReLoop/html/cancel.html")
                .build();

        try {
            // Generate payment link from PayOS
            CheckoutResponseData result = payOS.createPaymentLink(paymentData);
            System.out.println("Redirecting to: " + result.getCheckoutUrl());
            response.sendRedirect(result.getCheckoutUrl());  // Redirect user to PayOS for payment
        } catch (Exception e) {
            System.err.println("PayOS error: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can't create QR code: " + e.getMessage());
        }
    }

}
