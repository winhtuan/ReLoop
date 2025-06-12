package Model.DAO.conversation;

import Model.entity.conversation.Conversation;
import Utils.DBUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConversationDAO {

    public String generateConversationId() {
        String sql = "SELECT conversation_id FROM conversation ORDER BY conversation_id DESC LIMIT 1"; // MySQL dùng LIMIT 1
        String prefix = "CON";
        int nextId = 1;

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("conversation_id");
                int num = Integer.parseInt(lastId.substring(2));
                nextId = num + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return String.format("%s%04d", prefix, nextId); // Định dạng thành 'US00000X'
    }

    public String getOrCreateConversation(String user1Id, String user2Id, String productId) {
        try (Connection conn = Utils.DBUtils.getConnect()) {
            String sql = "SELECT conversation_id FROM conversation WHERE "
                    + "(sender_id = ? AND receiver_id = ? AND product_id = ?) OR "
                    + "(sender_id = ? AND receiver_id = ? AND product_id = ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user1Id);
            ps.setString(2, user2Id);
            ps.setString(3, productId);
            ps.setString(4, user2Id);
            ps.setString(5, user1Id);
            ps.setString(6, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("conversation_id");
            }

            // Nếu chưa tồn tại, tạo mới
            String conversationId = generateConversationId(); // Hàm sinh "CONV___"
            sql = "INSERT INTO conversation (conversation_id, sender_id, receiver_id, product_id) "
                    + "VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, conversationId);
            ps.setString(2, user1Id);
            ps.setString(3, user2Id);
            ps.setString(4, productId);
            ps.executeUpdate();

            return conversationId;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Conversation> getConversationsByUser(String userId) {
        List<Conversation> list = new ArrayList<>();
        String sql = "SELECT * FROM conversation WHERE sender_id = ? OR receiver_id = ?";
        try (Connection conn = Utils.DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Conversation(
                        rs.getString("conversation_id"),
                        rs.getString("sender_id"),
                        rs.getString("receiver_id"),
                        rs.getString("product_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ Thêm hàm hỗ trợ lấy người còn lại trong cuộc trò chuyện
    public static String getOtherParticipant(String conversationId, String currentUserId) {
        String sql = "SELECT sender_id, receiver_id FROM conversation WHERE conversation_id = ?";
        try (Connection conn = Utils.DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, conversationId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String userA = rs.getString("sender_id");
                String userB = rs.getString("receiver_id");
                if (userA.equals(currentUserId)) {
                    return userB;
                }
                if (userB.equals(currentUserId)) {
                    return userA;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy
    }

}
