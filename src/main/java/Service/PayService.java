package Service;

import Model.DAO.commerce.OrderDao;
import Model.DAO.commerce.OrderItemDAO;
import Model.DAO.pay.PaidServiceDAO;
import Model.DAO.pay.PaymentDao;
import Model.entity.commerce.OrderItem;
import Model.entity.commerce.OrderResult;
import Model.entity.pay.PaidService;
import Model.entity.pay.Voucher;
import Utils.AppConfig;
import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentData;

public class PayService {

    public String createPayOSPaymentLink(String orderId, int amount, String description)
            throws Exception {
        PayOS payOS = new PayOS(
                new AppConfig().get("payos.client_id"),
                new AppConfig().get("payos.api_key"),
                new AppConfig().get("payos.checksum_key")
        );
        PaymentData paymentData = PaymentData.builder()
                .orderCode(Long.valueOf(orderId.substring(3))) // Pass orderId as orderCode for PayOS
                .amount(amount)
                .description(description)
                .returnUrl("http://localhost:8080/ReLoop/PayOSCallback")
                .cancelUrl("http://localhost:8080/ReLoop/PayOSCallback")
                .build();
        CheckoutResponseData result = payOS.createPaymentLink(paymentData);
        return result.getCheckoutUrl();
    }

    public OrderResult createNormalOrder(HttpServletRequest request) {
        String buyerId = request.getParameter("buyerId");
        String address = request.getParameter("address");
        String vouchercode = request.getParameter("voucher");
        String shippingFeeStr = request.getParameter("shippingFee");

        int shipFee = 0;
        try {
            // Chỉ giữ số, loại dấu phẩy, chữ
            shippingFeeStr = shippingFeeStr.replaceAll("[^\\d]", ""); // chỉ giữ số
            if (!shippingFeeStr.isEmpty()) {
                shipFee = Integer.parseInt(shippingFeeStr);
            }
        } catch (NumberFormatException e) {
            shipFee = 0;
        }
        
        Voucher voucher = new OrderDao().getVoucherByCode(vouchercode);

        // Lấy từng item từ request (chỉ xử lý orders[0] cho 1 đơn/lần)
        int totalProductAmount = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        int itemIndex = 0;
        while (true) {
            String productId = request.getParameter("orders[0].productId[" + itemIndex + "]");
            String quantityStr = request.getParameter("orders[0].quantity[" + itemIndex + "]");
            String priceStr = request.getParameter("orders[0].price[" + itemIndex + "]");
            if (productId == null || quantityStr == null || priceStr == null) {
                break;
            }
            int quantity = Integer.parseInt(quantityStr);
            int price = Integer.parseInt(priceStr);
            totalProductAmount += (price * quantity);

            OrderItem item = new OrderItem();
            item.setOrderId(""); // sẽ set sau khi tạo order tổng
            item.setProductId(productId);
            item.setQuantity(quantity);
            item.setPrice(price);
            orderItems.add(item);
            itemIndex++;
        }

        int voucherDiscount = (voucher != null) ? voucher.getDiscountValue() : 0;
        int grandTotal = totalProductAmount + shipFee - voucherDiscount;
        if (grandTotal < 1) { // Phải lớn hơn 0.01 (PayOS min), kiểm tra là số nguyên.
            return null;
        }

        OrderDao orderDao = new OrderDao();
        String mainOrderId = orderDao.generateOrderId();
        boolean createdOrder = orderDao.createOrder(
                mainOrderId, buyerId, grandTotal, "pending", address, null,
                voucher != null ? voucher.getVoucherId() : null, voucherDiscount, shipFee);

        if (createdOrder) {
            for (OrderItem item : orderItems) {
                item.setOrderId(mainOrderId);
                new OrderItemDAO().insert(item);
            }
            try {
                new PaymentDao().createPayment(new PaymentDao().generatePaymentId(), mainOrderId, grandTotal, "pending");
            } catch (SQLException ex) {
                ex.printStackTrace();
                return null;
            }
            return new OrderResult(mainOrderId, grandTotal);
        }
        return null;
    }

    public OrderResult createPaidServiceOrder(String buyerId, String paidServiceId) {
        PaidService premium = new PaidServiceDAO().getPaidServiceById(paidServiceId);
        if (premium == null) {
            return null;
        }
        int amount = (int) premium.getPrice();
        String orderId = new OrderDao().generateOrderId();

        boolean created = new OrderDao().createOrder(orderId, buyerId, amount, "pending", null, null, null, 0, 0);

        if (created) {
            try {
                new PaymentDao().createPayment(new PaymentDao().generatePaymentId(), orderId, amount, "pending");
            } catch (SQLException ex) {
                Logger.getLogger(PayService.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
            return new OrderResult(orderId, amount);
        }
        return null;
    }

}
