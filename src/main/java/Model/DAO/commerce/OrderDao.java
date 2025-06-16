package Model.DAO.commerce;

import Utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class OrderDao {

    // Phương thức để tạo mã đơn hàng tự động (ORD0001)
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

    // Phương thức để tạo đơn hàng
    public boolean createOrder(String orderId, String userId, double amount, String status, String shippingAddress, Integer shippingMethod, String voucherId, double discountAmount) {
        
        // SQL query to insert a new order into the database
        String sql = "INSERT INTO orders (order_id, user_id, total_amount, status, shipping_address, shipping_method, "
                + "voucher_id, discount_amount, created_at, updated_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
        
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            // Set values for the prepared statement
            ps.setString(1, orderId);            // order_id
            ps.setString(2, userId);             // user_id
            ps.setDouble(3, amount);             // total_amount
            ps.setString(4, status);             // status (pending, paid, etc.)
            
            // Handle shipping address (could be null)
            if (shippingAddress == null) {
                ps.setNull(5, Types.VARCHAR);    // shipping_address is null if not provided
            } else {
                ps.setString(5, shippingAddress); // shipping_address provided
            }
            
            // Handle shipping method (could be null or set based on user's choice)
            if (shippingMethod == null) {
                ps.setNull(6, Types.INTEGER);    // shipping_method is null if not provided
            } else {
                ps.setInt(6, shippingMethod);   // shipping_method set based on user input
            }

            // Handle voucher ID (could be null)
            if (voucherId == null) {
                ps.setNull(7, Types.VARCHAR);    // voucher_id is null if no voucher applied
            } else {
                ps.setString(7, voucherId);      // voucher_id is set if a voucher is applied
            }
            
            // Set discount_amount (can be modified based on the discount logic)
            ps.setDouble(8, discountAmount);    // discount_amount (set to 0 if no discount)

            // Execute the update and check if it was successful
            int rows = ps.executeUpdate();
            return rows > 0;  // If rows affected > 0, return true, indicating success

        } catch (SQLException e) {
            e.printStackTrace();  // Log the exception for debugging
            return false;         // Return false if there's an error
        }
    }

    // Cập nhật trạng thái đơn hàng bằng orderId
    public boolean updateOrderStatusByOrderId(String orderId, String status) {
        String sql = "UPDATE orders SET status = ?, updated_at = NOW() WHERE order_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.toLowerCase());
            ps.setString(2, orderId);
            int rows = ps.executeUpdate();
            return rows > 0;  // true nếu có bản ghi được cập nhật
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật trạng thái đơn hàng bằng orderCode (nếu có sử dụng mã số đơn hàng khác)
    public boolean updateOrderStatusByOrderCode(Long orderCode, String status) {
        String sql = "UPDATE orders SET status = ?, updated_at = NOW() WHERE order_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.toLowerCase());
            ps.setString(2, String.valueOf(orderCode));
            int rows = ps.executeUpdate();
            return rows > 0;  // true nếu có bản ghi được cập nhật
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
