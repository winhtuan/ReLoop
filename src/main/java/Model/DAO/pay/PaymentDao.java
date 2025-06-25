package Model.DAO.pay;

import Utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class PaymentDao {

    public String generatePaymentId() {
        String sql = "SELECT pay_id FROM payments ORDER BY pay_id DESC LIMIT 1"; // MySQL dùng LIMIT 1
        String prefix = "PAY";
        int nextId = 1;

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("pay_id");
                int num = Integer.parseInt(lastId.substring(2));
                nextId = num + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return String.format("%s%04d", prefix, nextId); // Định dạng thành 'US00000X'
    }
    
    public boolean createPayment(String orderId, double amount, String status, LocalDateTime paidAt) throws SQLException {
        String payId = generatePaymentId();
        String sql = "INSERT INTO payments (pay_id, order_id, amount, status, paid_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnect();PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, payId);
            stmt.setString(2, orderId);
            stmt.setDouble(3, amount);
            stmt.setString(4, status); // Should be one of 'pending', 'paid', 'failed', 'refunded'
            if (paidAt != null) {
                stmt.setObject(5, paidAt);
            } else {
                stmt.setNull(5, java.sql.Types.TIMESTAMP);
            }
            int rows = stmt.executeUpdate();
            return rows == 1;
        }
    }
    
    public boolean updatePaymentStatus(String payId, String status) throws SQLException {
        String sql = "UPDATE payments SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE pay_id = ?";
        try (Connection conn = DBUtils.getConnect();PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setString(2, payId);
            int rows = stmt.executeUpdate();
            return rows == 1;
        }
    }
}
