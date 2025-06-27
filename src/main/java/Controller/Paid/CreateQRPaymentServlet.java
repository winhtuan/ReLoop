package Controller.Paid;

import Model.DAO.pay.PaidServiceDAO;
import Model.entity.commerce.OrderResult;
import Model.entity.pay.PaidService;
import Service.PayService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CreateQRPaymentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PayService payService = new PayService();
        String paidServiceId = request.getParameter("paidService_id");
        String buyerId = request.getParameter("buyerId");

        try {
            String payUrl = null;
            if (paidServiceId != null) {
                // TRƯỜNG HỢP 1: Thanh toán dịch vụ (nâng cấp premium v.v.)
                OrderResult orderResult = payService.createPaidServiceOrder(buyerId, paidServiceId);
                if (orderResult != null) {
                    PaidService premium = new PaidServiceDAO().getPaidServiceById(paidServiceId);
                    String desc = "Purchases " + premium.getServiceName();
                    payUrl = payService.createPayOSPaymentLink(orderResult.orderId, orderResult.amount, desc);
                }

            } else {
                // TRƯỜNG HỢP 2: Đặt hàng thông thường
                OrderResult orderResult = payService.createNormalOrder(request);
                if (orderResult == null) {
                    // Trả lỗi rõ ràng khi tổng tiền <= 0 hoặc không tạo được đơn
                    response.setContentType("text/html;charset=UTF-8");
                    response.getWriter().write("Lỗi: Tổng tiền phải lớn hơn 0 hoặc không tạo được đơn hàng!");
                    return;
                }
                String desc = "Payment for Reloop order";
                payUrl = payService.createPayOSPaymentLink(orderResult.orderId, orderResult.amount, desc);
            }

            if (payUrl != null) {
                response.sendRedirect(payUrl);
            } else {
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().write("Lỗi: Không tạo được order hoặc link thanh toán!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("Lỗi: " + e.getMessage());
        }
    }
}
