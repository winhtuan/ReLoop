package Model.DAO.conversation;

import Model.entity.conversation.Message;
import Utils.DBUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public static String generateMessageId() {
        String sql = "SELECT message_id FROM Messages ORDER BY message_id DESC LIMIT 1"; // MySQL dùng LIMIT 1
        String prefix = "MSG";
        int nextId = 1;

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("message_id");
                int num = Integer.parseInt(lastId.substring(3));
                nextId = num + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return String.format("%s%04d", prefix, nextId); // Định dạng thành 'US00000X'
    }

    // 1️⃣ Thêm tin nhắn (text) - ID là CHAR(7)
    public static String saveMessage(String conversationId, String senderId, String message) {
        String messageId = generateMessageId(); // giả sử có hàm sinh ID
        String sql = "INSERT INTO Messages (message_id, conversation_id, sender_id, content, SentAt, isRead, type) "
                + "VALUES (?, ?, ?, ?, NOW(), 0, 'text')";
        try (Connection conn = Utils.DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, messageId);
            ps.setString(2, conversationId);
            ps.setString(3, senderId);
            ps.setString(4, message);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            messageId = null; // lỗi thì trả null
        }
        return messageId;
    }

    // 2️⃣ Thêm tin nhắn (có type: 'img' hoặc 'text')
    public static String saveMessage(String conversationId, String senderId, String message, String type) {
        String messageId = generateMessageId(); // giả sử có hàm sinh ID
        String sql = "INSERT INTO Messages (message_id, conversation_id, sender_id, content, SentAt, isRead, type) "
                + "VALUES (?, ?, ?, ?, NOW(), 0, ?)";
        try (Connection conn = Utils.DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, messageId);
            ps.setString(2, conversationId);
            ps.setString(3, senderId);
            ps.setString(4, message);
            ps.setString(5, type);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            messageId = null;
        }
        return messageId;
    }

    // 3️⃣ Lấy user có tin nhắn chưa đọc
    public static List<String> getUsersWithUnreadMessages(String myUserId) {
        List<String> unreadSenders = new ArrayList<>();
        String sql = "SELECT DISTINCT sender_id "
                + "FROM Messages "
                + "WHERE sender_id != ? AND isRead = 0 AND conversation_id IN ("
                + "    SELECT conversation_id "
                + "    FROM conversation "
                + "    WHERE sender_id = ? OR receiver_id = ?"
                + ")";
        try (Connection conn = Utils.DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, myUserId);
            ps.setString(2, myUserId);
            ps.setString(3, myUserId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                unreadSenders.add(rs.getString("sender_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return unreadSenders;
    }

    // 4️⃣ Đánh dấu tin nhắn là đã đọc
    public static void markMessagesAsRead(String fromUserId, String toUserId) {
        String sql = "UPDATE Messages SET isRead = 1 WHERE sender_id = ? AND conversation_id IN ("
                + "SELECT conversation_id FROM conversation WHERE "
                + "(sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?))";
        try (Connection conn = Utils.DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fromUserId);
            ps.setString(2, fromUserId);
            ps.setString(3, toUserId);
            ps.setString(4, toUserId);
            ps.setString(5, fromUserId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 5️⃣ Lấy tin nhắn trong 1 cuộc trò chuyện
    public static List<Message> getMessagesByconversationId(String conversationId) {
        List<Message> list = new ArrayList<>();
        String sql = "SELECT * FROM Messages WHERE conversation_id = ? ORDER BY SentAt ASC";
        try (Connection conn = Utils.DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, conversationId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message msg = new Message(
                        rs.getString("message_id"),
                        rs.getString("conversation_id"),
                        rs.getString("sender_id"),
                        rs.getString("content"),
                        rs.getBoolean("isRead"),
                        rs.getString("type"),
                        rs.getTimestamp("SentAt"),
                        rs.getBoolean("is_recall")
                );
                list.add(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 6️⃣ Đánh dấu tin nhắn đã thu hồi
    public static boolean recallMessage(String messageId, String userId) {
        String sql = "UPDATE Messages SET is_recall = 1 WHERE message_id = ? AND sender_id = ?";
        try (Connection conn = Utils.DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, messageId);
            ps.setString(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy người nhận để thông báo thu hồi
    public static String getReceiverIdOfMessage(String messageId, String senderId) {
        String sql = "SELECT conversation_id FROM Messages WHERE message_id = ? AND sender_id = ?";
        try (Connection conn = Utils.DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, messageId);
            ps.setString(2, senderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String conversationId = rs.getString("conversation_id");
                return ConversationDAO.getOtherParticipant(conversationId, senderId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // không tìm thấy
    }

    public static void saveToSupporter(String userMessage, String userId) {
        if (userId == null || userMessage == null || userMessage.isBlank()) {
            return;
        }
        String conversationId = null;
        // 1. Tìm conversation giữa user và supporter
        String sqlFind = "SELECT c.conversation_id "
                + "FROM conversation c "
                + "JOIN users u ON c.receiver_id = u.user_id "
                + "WHERE c.sender_id = ? AND u.role = 'supporter' "
                + "LIMIT 1";

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sqlFind)) {

            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                conversationId = rs.getString("conversation_id");
            }

            // 2. Nếu đã có conversation thì lưu message
            if (conversationId != null) {
                System.out.println("njkahdhlashdkahsldkhalskd");
                saveMessage(conversationId, userId, userMessage, "text");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
    }
}
