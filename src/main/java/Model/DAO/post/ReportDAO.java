/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DAO.post;

import Model.entity.post.ProductReport;
import Utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 * @author Admin
 */
public class ReportDAO {
    private static final String SELECT_LAST_ID_SQL =
        "SELECT report_id FROM product_reports ORDER BY report_id DESC LIMIT 1";

    private static final String INSERT_SQL =
        "INSERT INTO product_reports (report_id, product_id, reporter_id, reason, description, status) VALUES ( ?, ?, ?, ?, ?, ?)";

    // Generate next report_id
    public String generateNextReportId() {
        String prefix = "REP";
        int nextId = 1;

        try (Connection conn = DBUtils.getConnect();
             PreparedStatement ps = conn.prepareStatement(SELECT_LAST_ID_SQL);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("report_id");
                if (lastId != null && lastId.startsWith(prefix)) {
                    String numPart = lastId.substring(prefix.length());
                    try {
                        nextId = Integer.parseInt(numPart) + 1;
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

    // Insert report to database
    public boolean insertReport(ProductReport report) {
        try (Connection conn = DBUtils.getConnect();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            ps.setString(1, report.getReportId());
            ps.setString(2, report.getProductId());
            ps.setString(3, report.getUserId());
            ps.setString(4, report.getReason());
            ps.setString(5, report.getDescription());
//            ps.setTimestamp(6, new java.sql.Timestamp(report.getReportedAt().getTime()));
            ps.setString(6, "pending");
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
