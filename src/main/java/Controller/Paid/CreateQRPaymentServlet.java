//package Controller.Paid;
//
//import Model.DAO.commerce.OrderDao;
//import Model.DAO.pay.payServiceDao;
//import Model.entity.pay.PaidService;
//import Utils.AppConfig;
//import java.io.IOException;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import vn.payos.PayOS;
//import vn.payos.type.CheckoutResponseData;
//import vn.payos.type.PaymentData;
//
//public class CreateQRPaymentServlet extends HttpServlet {
//
//    String clientId = new AppConfig().get("payos.client_id");
//    String apiKey = new AppConfig().get("payos.api_key");
//    String checksumKey = new AppConfig().get("payos.checksum_key");
//    PayOS payOS = new PayOS(clientId, apiKey, checksumKey);
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String id = request.getParameter("paidService_id"); 
//        String user_id = request.getParameter("user_id"); 
//        PaidService paidService = new payServiceDao().findById(id);
//        String description = paidService.getServiceName();
//        int amount = (int) paidService.getPrice() / 100;
//        Long orderCode = System.currentTimeMillis() / 1000;
//
//        // Tạo order trong database
//        String status = "pending"; // ban đầu
//        boolean created = new OrderDao().createOrder(user_id, amount, description, status);
//        if (!created) {
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create order");
//            return;
//        }
//        // Chuẩn bị dữ liệu gửi sang PayOS
//        PaymentData paymentData = PaymentData.builder()
//                .orderCode(orderCode)
//                .amount(amount)
//                .description(description)
//                .returnUrl("http://localhost:8080/html/success.html")
//                .cancelUrl("http://localhost:8080/html/cancel.html")
//                .build();
//
//        try {
//            CheckoutResponseData result = payOS.createPaymentLink(paymentData);
//            System.out.println("Redirecting to: " + result.getCheckoutUrl());
//            response.sendRedirect(result.getCheckoutUrl());
//        } catch (Exception e) {
//            System.err.println("PayOS error: " + e.getMessage());
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "can't create qr code: " + e.getMessage());
//        }
//    }
//
//}
