package Model.DAO.pay;

import Model.entity.pay.PaidService;
import Utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class payServiceDao {

    public String generatePaidServiceId() {
        String sql = "SELECT paid_id FROM paid_service ORDER BY paid_id DESC LIMIT 1"; // MySQL dùng LIMIT 1
        String prefix = "PRO";
        int nextId = 1;

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("paid_id");
                int num = Integer.parseInt(lastId.substring(2));
                nextId = num + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return String.format("%s%04d", prefix, nextId); // Định dạng thành 'US00000X'
    }

    public PaidService findById(String paidId) {
        PaidService paidService = null;
        String sql = "SELECT * FROM paid_service WHERE paid_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, paidId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    paidService = new PaidService();
                    paidService.setPaidId(rs.getString("paid_id"));
                    paidService.setServiceName(rs.getString("service_name"));
                    paidService.setPrice(rs.getDouble("price"));
                    paidService.setStartDate(rs.getTimestamp("start_date"));
                    paidService.setUsageTime(rs.getInt("usage_time"));
                    paidService.setEndDate(rs.getTimestamp("end_date"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paidService;
    }

    public void activatePremium(String userId, String paidId) {
        String sql = "UPDATE users "
                + "SET is_premium = 1, "
                + "premium_expiry = (SELECT end_date FROM paid_service WHERE paid_id = ?) "
                + "WHERE user_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, paidId);
            ps.setString(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
