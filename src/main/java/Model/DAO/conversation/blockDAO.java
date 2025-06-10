package Model.DAO.conversation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class blockDAO {

    // Hàm lấy connection
    private static Connection getConnection() throws SQLException {
        return Utils.DBUtils.getConnect();
    }

    // Kiểm tra xem blockerUserId đã block blockedUserId chưa
    public static boolean isBlocked(String blockerUserId, String blockedUserId) {
        String sql = "SELECT 1 FROM BlockUser_Conversation WHERE BlockerUserID = ? AND BlockedUser_ID = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, blockerUserId);
            ps.setString(2, blockedUserId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();  // Có kết quả nghĩa là đã block
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // lỗi thì coi như chưa block
        }
    }

    // Thêm bản ghi block user
    public static boolean blockUser(String blockerUserId, String blockedUserId) {
        if (isBlocked(blockerUserId, blockedUserId)) {
            return false; // Đã block rồi, không thêm nữa
        }

        String sql = "INSERT INTO BlockUser_Conversation (BlockerUserID, BlockedUser_ID) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, blockerUserId);
            ps.setString(2, blockedUserId);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa bản ghi unblock user
    public static boolean unblockUser(String blockerUserId, String blockedUserId) {
        String sql = "DELETE FROM BlockUser_Conversation WHERE BlockerUserID = ? AND BlockedUser_ID = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, blockerUserId);
            ps.setString(2, blockedUserId);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
