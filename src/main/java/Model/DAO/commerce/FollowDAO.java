package Model.DAO.commerce;

import Model.entity.post.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import Utils.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import Model.DAO.post.ProductImageDao;
import Model.entity.auth.User;
import Model.entity.commerce.Follow;
import java.sql.SQLException;

public class FollowDAO {

    // Thêm theo dõi (followerId -> followingId)
    public void addFollowing(String followerId, String followingId) throws SQLException {
        String sql = "INSERT INTO following_follower (follower_id, following_id) VALUES (?, ?)";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, followerId);
            ps.setString(2, followingId);
            ps.executeUpdate();
        }
    }

    // Hủy theo dõi
    public boolean unFollow(String followerId, String followingId) throws SQLException {
        String sql = "DELETE FROM following_follower WHERE follower_id = ? AND following_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, followerId);
            ps.setString(2, followingId);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    public List<User> getFollowingUsers(String followerId) throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT u.* FROM following_follower f JOIN users u ON f.following_id = u.user_id WHERE f.follower_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, followerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getString("user_id"));
                u.setFullName(rs.getString("FullName"));
                u.setSrcImg(rs.getString("img"));
                list.add(u);
            }
        }
        return list;
    }

    public List<User> getFollowerUsers(String followingId) throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT u.* FROM following_follower f JOIN users u ON f.follower_id = u.user_id WHERE f.following_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, followingId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getString("user_id"));
                u.setFullName(rs.getString("FullName"));
                u.setSrcImg(rs.getString("img"));
                list.add(u);
            }
        }
        return list;
    }

    // Kiểm tra đã theo dõi chưa
    public boolean isFollowing(String followerId, String followingId) throws SQLException {
        String sql = "SELECT 1 FROM following_follower WHERE follower_id = ? AND following_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, followerId);
            ps.setString(2, followingId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public int countFollowing(String userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM following_follower WHERE follower_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public int countFollowers(String userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM following_follower WHERE following_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

}
