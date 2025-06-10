package Model.DAO.commerce;

import Utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class OrderDao {

    public String generateOrderId() {
        String sql = "SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1";
        String prefix = "ORD";
        int nextId = 1;

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("order_id");
                if (lastId != null && lastId.length() > prefix.length()) {
                    String numericPart = lastId.substring(prefix.length());
                    try {
                        int num = Integer.parseInt(numericPart);
                        nextId = num + 1;
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return String.format("%s%04d", prefix, nextId);
    }

    public boolean createOrder(String userId, double amount, String paidService, String status) {
        String orderId = generateOrderId();
        String sql = "INSERT INTO orders (order_id, user_id, service_name, total_amount, status, shipping_address, created_at, updated_at, discount_amount) "
                + "VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW(), ?)";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, orderId);
            ps.setString(2, userId);
            ps.setString(3, paidService);
            ps.setDouble(4, amount); // Sửa từ setInt -> setDouble
            ps.setString(5, status);
            ps.setNull(6, Types.VARCHAR); // shipping_address
            ps.setDouble(7, 0); // discount_amount mặc định = 0

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateOrderStatusByOrderCode(Long orderCode, String status) {
        String lowerStatus = status.toLowerCase();

        String sql = "UPDATE orders SET status = ?, updated_at = NOW() WHERE order_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lowerStatus);
            ps.setString(2, String.valueOf(orderCode));

            int rows = ps.executeUpdate();
            return rows > 0; // true nếu có bản ghi được cập nhật
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
