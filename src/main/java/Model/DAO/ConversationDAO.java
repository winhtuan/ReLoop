package Model.DAO;

import Model.Entity.Conversation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConversationDAO {

    public static int getOrCreateConversation(int user1Id, int user2Id) {
        try (Connection conn = Utils.DBUtils.getConnect()) {
            String sql = "SELECT id FROM conversation WHERE " +
                         "(sender_id = ? AND receiver_id = ?) OR (sender_id= ? AND receiver_id= ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, user1Id);
            ps.setInt(2, user2Id);
            ps.setInt(3, user2Id);
            ps.setInt(4, user1Id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");

            // Nếu chưa tồn tại thì tạo mới
            sql = "INSERT INTO conversation (sender_id, receiver_id) VALUES (?, ?)";
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, user1Id);
            ps.setInt(2, user2Id);
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static List<Conversation> getConversationsByUser(int userId) {
        List<Conversation> list = new ArrayList<>();
        try (Connection conn = Utils.DBUtils.getConnect()) {
            String sql = "SELECT * FROM conversation WHERE sender_id = ? OR receiver_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Conversation(
                    rs.getInt("id"),
                    rs.getInt("sender_id"),
                    rs.getInt("receiver_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ Thêm hàm hỗ trợ lấy người còn lại trong cuộc trò chuyện
    public static int getOtherParticipant(int conversationId, int currentUserId) {
        try (Connection conn = Utils.DBUtils.getConnect()) {
            String sql = "SELECT sender_id, receiver_id FROM conversation WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, conversationId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int userA = rs.getInt("sender_id");
                int userB = rs.getInt("receiver_id");
                if (userA == currentUserId) return userB;
                if (userB == currentUserId) return userA;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Không tìm thấy
    }

    public static void main(String[] args) {
        System.out.println(getOrCreateConversation(17, 18));
//        System.out.println(getOtherParticipant(1, 1)); // test thử
    }
}
