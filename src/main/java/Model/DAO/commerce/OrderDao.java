package Model.DAO.commerce;

import Model.entity.commerce.Order;
import Model.entity.pay.Voucher;
import Utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

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
    public boolean createOrder(String orderId, String userId, double amount, String status, String shippingAddress, Integer shippingMethod, String voucherId, double discountAmount, int shipFee) {
        String sql = "INSERT INTO orders (order_id, user_id, total_amount, status, shipping_address, shipping_method, "
                + "voucher_id, discount_amount, created_at, updated_at, shipfee) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW(), ?)";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderId);
            ps.setString(2, userId);
            ps.setDouble(3, amount);
            ps.setString(4, status);
            if (shippingAddress == null) {
                ps.setNull(5, Types.VARCHAR);
            } else {
                ps.setString(5, shippingAddress);
            }
            if (shippingMethod == null) {
                ps.setNull(6, Types.INTEGER);
            } else {
                ps.setInt(6, shippingMethod);
            }
            if (voucherId == null) {
                ps.setNull(7, Types.VARCHAR);
            } else {
                ps.setString(7, voucherId);
            }
            ps.setDouble(8, discountAmount);
            ps.setInt(9, shipFee);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
                    order.setShipfee(rs.getInt("shipfee"));
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
        if (order.getOrderId() == null || order.getOrderId().isBlank()) {
            order.setOrderId(generateOrderId());
        }

        String sql = "INSERT INTO orders (order_id, user_id, total_amount, status, shipping_address, shipping_method, voucher_id, discount_amount, created_at, updated_at, shipfee) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW(), ?)";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, order.getOrderId());
            ps.setString(2, order.getUserId());
            ps.setDouble(3, order.getTotalAmount());
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
            ps.setDouble(8, order.getDiscountAmount());

            // shipfee
            ps.setInt(9, order.getShipfee());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Voucher> getAllVoucher() {
        List<Voucher> list = new ArrayList<>();
        String sql = "SELECT * FROM vouchers";
        try (Connection con = DBUtils.getConnect(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Voucher v = new Voucher();
                v.setVoucherId(rs.getString("voucher_id"));
                v.setCode(rs.getString("code"));
                v.setDescription(rs.getString("description"));
                v.setDiscountValue(rs.getInt("discount_value"));
                v.setMinOrderAmount(rs.getInt("min_order_amount"));
                v.setStartDate(rs.getTimestamp("start_date"));
                v.setEndDate(rs.getTimestamp("end_date"));
                v.setUsageLimit(rs.getInt("usage_limit"));
                v.setUsedCount(rs.getInt("used_count"));
                v.setActive(rs.getBoolean("is_active"));
                list.add(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Voucher getVoucherByCode(String code) {
        String sql = "SELECT * FROM vouchers WHERE code = ?";
        try (Connection con = DBUtils.getConnect(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Voucher v = new Voucher();
                    v.setVoucherId(rs.getString("voucher_id"));
                    v.setCode(rs.getString("code"));
                    v.setDescription(rs.getString("description"));
                    v.setDiscountValue(rs.getInt("discount_value"));
                    v.setMinOrderAmount(rs.getInt("min_order_amount"));
                    v.setStartDate(rs.getTimestamp("start_date"));
                    v.setEndDate(rs.getTimestamp("end_date"));
                    v.setUsageLimit(rs.getInt("usage_limit"));
                    v.setUsedCount(rs.getInt("used_count"));
                    v.setActive(rs.getBoolean("is_active"));
                    return v;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
