package Controller.Paid;

import Model.DAO.auth.UserDao;
import Model.DAO.commerce.OrderDao;
import Model.entity.commerce.Order;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;

public class PayOSCallbackServlet extends HttpServlet {

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
}
