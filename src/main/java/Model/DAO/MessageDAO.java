package Model.DAO;

import Model.Entity.Message;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public static int saveMessage(int conversationId, int senderId, String message) {
        int messageId = -1;
        try (Connection conn = Utils.DBUtils.getConnect()) {
            String sql = "INSERT INTO Messages (conversation_id, sender_id, content, SentAt,isRead,type) VALUES (?, ?, ?, GETDATE(),0,'text')";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, conversationId);
            ps.setInt(2, senderId);
            ps.setString(3, message);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                messageId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messageId;
    }
    public static int saveMessage(int conversationId, int senderId, String message,String type) {
        int messageId = -1;
        try (Connection conn = Utils.DBUtils.getConnect()) {
            String sql = "INSERT INTO Messages (conversation_id, sender_id, content, SentAt,isRead,type) VALUES (?, ?, ?, GETDATE(),0,?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, conversationId);
            ps.setInt(2, senderId);
            ps.setString(3, message);
            ps.setString(4, type);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                messageId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messageId;
    }

    public static List<Integer> getUsersWithUnreadMessages(int myUserId) {
        List<Integer> unreadSenders = new ArrayList<>();
        String sql
                = "SELECT DISTINCT sender_id "
                + "FROM Messages "
                + "WHERE sender_id != ? AND isRead = 0 AND conversation_id IN ( "
                + "    SELECT conversation_id "
                + "    FROM conversation "
                + "    WHERE sender_id = ? OR receiver_id = ? "
                + ")";

        try (Connection conn = Utils.DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, myUserId);
            ps.setInt(2, myUserId);
            ps.setInt(3, myUserId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                unreadSenders.add(rs.getInt("sender_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return unreadSenders;
    }

    public static void markMessagesAsRead(int fromUserId, int toUserId) {
        String sql = "UPDATE Messages SET isRead = 1 WHERE sender_id = ? AND conversation_id IN ("
                + "SELECT conversation_id FROM conversation WHERE "
                + "(sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?))";
        try (Connection conn = Utils.DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, fromUserId); // Sender
            ps.setInt(2, fromUserId);
            ps.setInt(3, toUserId);
            ps.setInt(4, toUserId);
            ps.setInt(5, fromUserId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Message> getMessagesByconversationId(int conversationId) {
        List<Message> list = new ArrayList<>();
        try (Connection conn = Utils.DBUtils.getConnect()) {
            String sql = "SELECT * FROM Messages WHERE conversation_id = ? ORDER BY SentAt ASC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, conversationId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message msg = new Message(
                        rs.getInt("Message_id"),
                        rs.getInt("conversation_id"),
                        rs.getInt("sender_id"),
                        rs.getString("content"),
                        rs.getTimestamp("SentAt"),
                        rs.getBoolean("is_recall"),
                        rs.getBoolean("isRead"),
                        rs.getString("type")
                );
                list.add(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Đánh dấu là đã thu hồi
    public static boolean recallMessage(int messageId, int userId) {
        String sql = "UPDATE Messages SET is_recall = 1 WHERE Message_id = ? AND sender_id = ?";
        try (Connection conn = Utils.DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, messageId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy người nhận để thông báo thu hồi
    public static int getReceiverIdOfMessage(int messageId, int senderId) {
        String sql = "SELECT conversation_id FROM Messages WHERE Message_id = ? AND sender_id = ?";
        try (Connection conn = Utils.DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, messageId);
            ps.setInt(2, senderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int conversationId = rs.getInt("conversation_id");
                return ConversationDAO.getOtherParticipant(conversationId, senderId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // không tìm thấy
    }

    public static void main(String[] args) throws SQLException {
        
    }
}
