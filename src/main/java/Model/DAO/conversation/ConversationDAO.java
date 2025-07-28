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
                int num = Integer.parseInt(lastId.substring(3));
                nextId = num + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return String.format("%s%04d", prefix, nextId); // Định dạng thành 'US00000X'
    }

    /**
     * Tìm cuộc trò chuyện giữa user1 và user2 theo productId; nếu chưa có thì
     * tạo mới và trả về đối tượng Conversation tương ứng.
     */
    public Conversation getConversation(String user1Id, String user2Id) {
        String sql = "SELECT conversation_id, sender_id, receiver_id, product_id FROM conversation WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?)";

        try (Connection conn = Utils.DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {

            // Thiết lập tham số cho cả hai chiều người gửi/người nhận
            ps.setString(1, user1Id);
            ps.setString(2, user2Id);
            ps.setString(3, user2Id);
            ps.setString(4, user1Id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Conversation(
                            rs.getString("conversation_id"),
                            rs.getString("sender_id"),
                            rs.getString("receiver_id"),
                            rs.getString("product_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();          // Hoặc log lỗi tùy dự án của bạn
            return null;
        }
        return null;
    }

    public Conversation createConversation(String user1, String user2, String productID) throws SQLException {
        String conversationId = generateConversationId();   // "CONV___" chẳng hạn
        String sql
                = "INSERT INTO conversation (conversation_id, sender_id, receiver_id,product_id)"
                + "VALUES (?, ?, ?, ?)";

        try (Connection conn = Utils.DBUtils.getConnect(); PreparedStatement insertPs = conn.prepareStatement(sql)) {
            insertPs.setString(1, conversationId);
            insertPs.setString(2, user1);   // Giả định user1 là người gửi ban đầu
            insertPs.setString(3, user2);
            insertPs.setString(4, productID);
            insertPs.executeUpdate();
        }
        return new Conversation(conversationId, user1, user2, productID);
    }

    public String getOrCreateConversation(String user1Id, String user2Id, String productId) {
        try (Connection conn = Utils.DBUtils.getConnect()) {
            String sql = "SELECT conversation_id FROM conversation WHERE "
                    + "(sender_id = ? AND receiver_id = ? ) OR "
                    + "(sender_id = ? AND receiver_id = ? )";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user1Id);
            ps.setString(2, user2Id);
            ps.setString(3, user2Id);
            ps.setString(4, user1Id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("conversation_id");
            }

            // Nếu chưa tồn tại, tạo mới
            String conversationId = generateConversationId(); // Hàm sinh "CONV___"
            sql = "INSERT INTO conversation (conversation_id, sender_id, receiver_id, product_id ) "
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

    public boolean updateProductId(String conversationId, String newProductId) {
        String sql = "UPDATE conversation SET product_id = ? WHERE conversation_id = ?";

        try (Connection conn = Utils.DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newProductId);
            ps.setString(2, conversationId);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0; // Trả về true nếu có hàng được cập nhật

        } catch (SQLException e) {
            e.printStackTrace();  // hoặc dùng log framework
            return false;
        }
    }

    public String getLeastBusySupporterId() {
        String sql = "SELECT u.user_id " +
             "FROM users u " +
             "LEFT JOIN ( " +
             "    SELECT sender_id AS user_id FROM conversation " +
             "    UNION ALL " +
             "    SELECT receiver_id FROM conversation " +
             ") conv ON u.user_id = conv.user_id " +
             "WHERE u.role = 'supporter' " +
             "GROUP BY u.user_id " +
             "ORDER BY COUNT(conv.user_id) ASC " +
             "LIMIT 1";

        try (Connection conn = Utils.DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSupporterInConversation(String customerId) {
        String sql = "SELECT u.user_id "
                + "FROM conversation c "
                + "JOIN users u ON "
                + "(u.user_id = c.sender_id AND c.receiver_id = ?) OR "
                + "(u.user_id = c.receiver_id AND c.sender_id = ?) "
                + "WHERE u.role = 'supporter' "
                + "LIMIT 1";

        try (Connection conn = Utils.DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customerId);
            ps.setString(2, customerId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("user_id");  // Trả về supporter
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy hoặc không có supporter
    }

    public static void main(String[] args) {
        System.out.println(new ConversationDAO().getConversation("CUS0001", "CUS0002").toString());
    }
}
