package Model.DAO.pay;

import Utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TransactionDAO {

    // Phương thức để tạo mã giao dịch mới (transaction_id)
    public String generateTransactionId() {
        String sql = "SELECT transaction_id FROM transaction_logs ORDER BY transaction_id DESC LIMIT 1";
        String prefix = "TRA";
        int nextId = 1;

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String lastId = rs.getString("transaction_id");
                int num = Integer.parseInt(lastId.substring(3)); // Tách "TRA" và lấy số
                nextId = num + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return String.format("%s%04d", prefix, nextId); // Trả về mã giao dịch mới với định dạng TRA0001
    }

    // Phương thức để ghi nhận giao dịch vào bảng transaction_logs
    public boolean logTransaction(String userId, String transactionType,
                                  double amount, double balanceBefore, double balanceAfter,
                                  String referenceId, String description, String status) {
        // SQL để chèn dữ liệu vào bảng transaction_logs
        String sql = "INSERT INTO transaction_logs (transaction_id, user_id, type, amount, balance_before, balance_after, reference_id, description, created_at, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtils.getConnect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Tạo mã giao dịch mới (transaction_id)
            String transactionId = generateTransactionId();

            // Thời gian hiện tại cho created_at
            Timestamp createdAt = new Timestamp(System.currentTimeMillis());

            // Cài đặt các tham số cho PreparedStatement
            stmt.setString(1, transactionId);
            stmt.setString(2, userId);
            stmt.setString(3, transactionType); // deposit, withdraw, refund, payment, adjustment
            stmt.setDouble(4, amount);
            stmt.setDouble(5, balanceBefore);
            stmt.setDouble(6, balanceAfter);
            stmt.setString(7, referenceId);
            stmt.setString(8, description);
            stmt.setTimestamp(9, createdAt);
            stmt.setString(10, status); // pending, success, failed

            // Thực thi câu lệnh chèn dữ liệu
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
