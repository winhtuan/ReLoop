package Model.DAO.commerce;

import Model.entity.commerce.Notification;
import Utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public String generateNoticId() {
        String sql = "SELECT noti_id FROM notification ORDER BY noti_id DESC LIMIT 1";
        String prefix = "NOT";
        int nextId = 1;

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String lastId = rs.getString("noti_id");
                if (lastId != null && lastId.length() > prefix.length()) {
                    String numericPart = lastId.substring(prefix.length());
                    try {
                        int num = Integer.parseInt(numericPart);
                        nextId = num + 1;
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

    public void markAsRead(String notificationId) throws SQLException {
        String sql = "UPDATE notification SET is_read = TRUE WHERE noti_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, notificationId);
            ps.executeUpdate();
        }
    }

    public void deleteNotification(String notificationId) throws SQLException {
        String sql = "DELETE FROM notification WHERE noti_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, notificationId);
            ps.executeUpdate();
        }
    }

    public void createNotification(Notification notification) throws SQLException {
        String sql = "INSERT INTO notification (noti_id, user_id, title, content, link, is_read, created_at, type) "
                + "VALUES (?, ?, ?, ?, ?, FALSE, NOW(), ?)";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, notification.getNotiId());
            ps.setString(2, notification.getUserId());
            ps.setString(3, notification.getTitle());
            ps.setString(4, notification.getContent());
            ps.setString(5, notification.getLink());
            ps.setString(6, notification.getType());
            ps.executeUpdate();
        }
    }

    public void createMessageNotification(String fromUserId, String fromUsername, String toUserId, String content) throws SQLException {
        Notification noti = new Notification();
        noti.setNotiId(generateNoticId());
        noti.setUserId(toUserId);
        noti.setTitle("New message");
        noti.setContent("You have a new message from " + fromUsername + ": " + content);
        noti.setLink("/chat?user=" + fromUserId);
        noti.setType("MESSAGE");
        createNotification(noti);
        // Push realtime
        Controller.NotificateSocket.pushNotification(
                toUserId,
                noti.getTitle(),
                noti.getContent(),
                noti.getLink()
        );
    }

    public void createOrderStatusNotification(String toUserId, String orderId, String newStatus) throws SQLException {
        Notification noti = new Notification();
        noti.setNotiId(generateNoticId());
        noti.setUserId(toUserId);
        noti.setTitle("Order Status Updated");
        noti.setContent("Your order #" + orderId + " status has been updated to: " + newStatus + ".");
        noti.setLink("/s_orderHistory?orderId=" + orderId);
        noti.setType("ORDER");
        createNotification(noti);
        // Push realtime notification
        Controller.NotificateSocket.pushNotification(
                toUserId,
                noti.getTitle(),
                noti.getContent(),
                noti.getLink()
        );
    }

    public List<Notification> getUserNotification(String userId) throws SQLException {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notification WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notification n = new Notification();
                    n.setNotiId(rs.getString("noti_id"));
                    n.setUserId(rs.getString("user_id"));
                    n.setTitle(rs.getString("title"));
                    n.setContent(rs.getString("content"));
                    n.setLink(rs.getString("link"));
                    n.setRead(rs.getBoolean("is_read"));
                    n.setType(rs.getString("type"));
                    n.setCreatedAt(rs.getTimestamp("created_at"));
                    list.add(n);
                }
            }
        }
        return list;
    }

}
