package Model.DAO.pay;

import Utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentDao {

    public String generatePaymentId() {
        String sql = "SELECT pay_id FROM payments ORDER BY pay_id DESC LIMIT 1";
        String prefix = "PAY";
        int nextId = 1;

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String lastId = rs.getString("pay_id");
                if (lastId != null && lastId.startsWith(prefix)) {
                    String numericPart = lastId.substring(prefix.length());
                    try {
                        int num = Integer.parseInt(numericPart);
                        nextId = num + 1;
                    } catch (NumberFormatException e) {
                        // Ghi log ra lỗi này, và tiếp tục với nextId = 1 (bỏ qua bản ghi lỗi)
                        System.err.println("Lỗi định dạng paymentId: " + lastId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return String.format("%s%04d", prefix, nextId);
    }

    public boolean createPayment(String payId, String orderId, double amount, String status) throws SQLException {
        String sql = "INSERT INTO payments (pay_id, order_id, amount, status, paid_at, created_at, updated_at) "
                + "VALUES (?, ?, ?, ?, NULL, NOW(), NOW())";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, payId);
            ps.setString(2, orderId);
            ps.setDouble(3, amount);
            ps.setString(4, status);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updatePaymentStatus(String payId, String status) throws SQLException {
        String sql = "UPDATE payments SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE pay_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setString(2, payId);
            int rows = stmt.executeUpdate();
            return rows == 1;
        }
    }

    public String getPaymentIdByOrderId(String orderId) {
        String paymentId = null;
        String sql = "SELECT pay_id FROM payments WHERE order_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    paymentId = rs.getString("pay_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paymentId;
    }

}
