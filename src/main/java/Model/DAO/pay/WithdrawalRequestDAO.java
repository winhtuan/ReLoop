/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DAO.pay;

import Model.entity.pay.WithdrawalRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WithdrawalRequestDAO {

    private Connection conn;

    public WithdrawalRequestDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean insertWithdrawalRequest(WithdrawalRequest request) {
        String sql = "INSERT INTO WithdrawalRequest (user_id, amount, bank_code, account_number, account_name, add_info, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, 'PENDING')";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, request.getUserId());
            stmt.setInt(2, request.getAmount());
            stmt.setString(3, request.getBankCode());
            stmt.setString(4, request.getAccountNumber());
            stmt.setString(5, request.getAccountName());
            stmt.setString(6, request.getAddInfo());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Hoặc log
            return false;
        }
    }

    public List<WithdrawalRequest> getAllRequests() {
        List<WithdrawalRequest> requests = new ArrayList<>();

        String sql = "SELECT * FROM WithdrawalRequest ORDER BY created_at DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                WithdrawalRequest request = new WithdrawalRequest();
                request.setId(rs.getInt("id"));
                request.setUserId(rs.getString("user_id"));
                request.setAmount(rs.getInt("amount"));
                request.setBankCode(rs.getString("bank_code"));
                request.setAccountNumber(rs.getString("account_number"));
                request.setAccountName(rs.getString("account_name"));
                request.setAddInfo(rs.getString("add_info"));
                request.setStatus(rs.getString("status"));
                request.setCreatedAt(rs.getTimestamp("created_at"));
                request.setUpdatedAt(rs.getTimestamp("updated_at"));

                requests.add(request);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // hoặc log lỗi
        }

        return requests;
    }
    public boolean rejectRequestById(int requestId) {
    String sql = "UPDATE WithdrawalRequest SET status = 'REJECTED', updated_at = CURRENT_TIMESTAMP WHERE id = ? AND status = 'PENDING'";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, requestId);
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        e.printStackTrace(); // hoặc log lỗi
        return false;
    }
}
public boolean approveRequestById(int requestId) {
    String sql = "UPDATE WithdrawalRequest SET status = 'APPROVED', updated_at = CURRENT_TIMESTAMP WHERE id = ? AND status = 'PENDING'";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, requestId);
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        e.printStackTrace(); // hoặc log lỗi
        return false;
    }
}

}
