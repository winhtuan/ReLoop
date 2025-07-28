package Model.DAO.commerce;

import Utils.*;
import Model.entity.commerce.Feedback;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for managing feedback records.
 */
public class FeedbackDao {

    public boolean saveFeedback(String orderId, String userId, int rating, String comment) throws SQLException {
        // Kiểm tra nếu đã feedback
        if (hasFeedback(orderId, userId)) {
            return false; // Trả về false nếu đã feedback
        }

        String sql = "INSERT INTO feedback (feedback_id, order_id, user_id, rating, comment) VALUES (?, ?, ?, ?, ?)";
        String feedbackId = generateFeedbackId();

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, feedbackId);
            ps.setString(2, orderId);
            ps.setString(3, userId);
            ps.setInt(4, rating);
            ps.setString(5, comment != null ? comment : "");
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    public boolean hasFeedback(String orderId, String userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM feedback WHERE order_id = ? AND user_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderId);
            ps.setString(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Trả về true nếu đã có feedback
                }
            }
        }
        return false;
    }

    public List<Feedback> getFeedbackByProductId(String productId) throws SQLException {
        String sql = "SELECT f.feedback_id, f.user_id, f.rating, f.comment, f.created_at, u.FullName "
                + "FROM feedback f "
                + "JOIN orders o ON f.order_id = o.order_id "
                + "JOIN order_items oi ON o.order_id = oi.order_id "
                + "JOIN users u ON f.user_id = u.user_id "
                + "WHERE oi.product_id = ? "
                + "ORDER BY f.created_at DESC";
        List<Feedback> feedbackList = new ArrayList<>();

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productId);
            System.out.println("Executing query for productId: " + productId); // Debug
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Feedback feedback = new Feedback();
                    feedback.setFeedbackId(rs.getString("feedback_id"));
                    feedback.setUserId(rs.getString("user_id"));
                    feedback.setRating(rs.getInt("rating"));
                    feedback.setComment(rs.getString("comment"));
                    feedback.setCreatedAt(rs.getTimestamp("created_at"));
                    feedback.setFullName(rs.getString("FullName"));
                    feedbackList.add(feedback);
                    System.out.println("Found feedback: " + feedback.getFeedbackId() + ", Rating: " + feedback.getRating()); // Debug
                }
            }
        }
        return feedbackList;
    }

    private String generateFeedbackId() throws SQLException {
        String prefix = "FDB";
        String sql = "SELECT COALESCE(MAX(SUBSTRING(feedback_id, 4)), '0000') AS last_number FROM feedback";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int lastNumber = Integer.parseInt(rs.getString("last_number"));
                int newNumber = lastNumber + 1;
                return prefix + String.format("%04d", newNumber);
            }
            return prefix + "0001";
        }
    }
}
