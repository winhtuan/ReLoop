package Model.DAO.commerce;

import Model.entity.commerce.Order;
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

    public Order getOrderById(String orderId) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = new Order();
                    order.setOrderId(rs.getString("order_id"));
                    order.setUserId(rs.getString("user_id"));
                    order.setTotalAmount(rs.getInt("total_amount"));
                    order.setStatus(rs.getString("status"));
                    order.setShippingAddress(rs.getString("shipping_address"));
                    order.setShippingMethod(rs.getObject("shipping_method") != null ? rs.getInt("shipping_method") : null);
                    order.setVoucherId(rs.getString("voucher_id"));
                    order.setDiscountAmount(rs.getInt("discount_amount"));
                    order.setCreatedAt(rs.getTimestamp("created_at"));
                    order.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return order;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy
    }
    // Thêm mới đơn hàng dựa trên object Order

    public boolean insert(Order order) {
        // Nếu orderId chưa có –> sinh tự động
        if (order.getOrderId() == null || order.getOrderId().isBlank()) {
            order.setOrderId(generateOrderId());
        }

        String sql = "INSERT INTO orders (order_id, user_id, total_amount, status,shipping_address, shipping_method,voucher_id, discount_amount,created_at, updated_at)VALUES(?,?,?,?,?,?,?,?,NOW(),NOW())";

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, generateOrderId());
            ps.setString(2, order.getUserId());
            ps.setInt(3, order.getTotalAmount());  
            ps.setString(4, order.getStatus());

            // shipping_address
            if (order.getShippingAddress() == null) {
                ps.setNull(5, Types.VARCHAR);
            } else {
                ps.setString(5, order.getShippingAddress());
            }

            // shipping_method
            if (order.getShippingMethod() == null) {
                ps.setNull(6, Types.INTEGER);
            } else {
                ps.setInt(6, order.getShippingMethod());
            }

            // voucher_id
            if (order.getVoucherId() == null) {
                ps.setNull(7, Types.VARCHAR);
            } else {
                ps.setString(7, order.getVoucherId());
            }

            // discount_amount
            ps.setInt(8, order.getDiscountAmount());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
