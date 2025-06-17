package Model.DAO.pay;

import Model.entity.pay.PaidService;
import Utils.DBUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaidServiceDAO {
    public boolean addPaidService(PaidService paidService) {
        String sql = "INSERT INTO PaidService (paid_id, service_name, price, description, usage_time) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, paidService.getPaidId());
            stmt.setString(2, paidService.getServiceName());
            stmt.setInt(3, paidService.getPrice());
            stmt.setString(4, paidService.getDescription());
            stmt.setInt(5, paidService.getUsageTime());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deletePaidService(String paidId) {
        String sql = "DELETE FROM PaidService WHERE paid_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, paidId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updatePaidService(PaidService paidService) {
        String sql = "UPDATE PaidService SET service_name = ?, price = ?, description = ?, usage_time = ? WHERE paid_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, paidService.getServiceName());
            stmt.setInt(2, paidService.getPrice());
            stmt.setString(3, paidService.getDescription());
            stmt.setInt(4, paidService.getUsageTime());
            stmt.setString(5, paidService.getPaidId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public PaidService getPaidServiceById(String paidId) {
        String sql = "SELECT * FROM PaidService WHERE paid_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, paidId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToPaidService(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<PaidService> getAllPaidServices() {
        List<PaidService> paidServices = new ArrayList<>();
        String sql = "SELECT * FROM PaidService";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                paidServices.add(mapResultSetToPaidService(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paidServices;
    }
    private PaidService mapResultSetToPaidService(ResultSet rs) throws SQLException {
        String paidId = rs.getString("paid_id");
        String serviceName = rs.getString("service_name");
        int price = rs.getInt("price");
        String description = rs.getString("description");
        int usageTime = rs.getInt("usage_time");
        return new PaidService(paidId, serviceName, price, description, usageTime);
    }
}
