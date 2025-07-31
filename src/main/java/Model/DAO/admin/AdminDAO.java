package Model.DAO.admin;

import Model.DAO.post.CategoryStats;
import Model.entity.auth.User;
import Model.entity.pay.MonthRevenue;
import Model.entity.post.Category;
import Utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class AdminDAO {

    public List<User> getListAccount() {
        String sql = "SELECT u.user_id, u.FullName, a.email, u.PhoneNumber, u.role FROM users u JOIN account a ON u.user_id = a.user_id WHERE a.is_block = 0 LIMIT 0, 1000;";

        List<User> list = new ArrayList<>();

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getString("user_id"));
                user.setFullName(rs.getString("FullName"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("PhoneNumber"));
                user.setRole(rs.getString("role"));
                list.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách tài khoản chưa bị khóa: " + e.getMessage());
        }

        return list;
    }

    public List<User> getListBlockedAccount() {
        String sql = "SELECT u.user_id, u.FullName, a.email, u.PhoneNumber, u.role FROM users u JOIN account a ON u.user_id = a.user_id WHERE a.is_block = 1 LIMIT 0, 1000;";

        List<User> list = new ArrayList<>();

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getString("user_id"));
                user.setFullName(rs.getString("FullName"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("PhoneNumber"));
                user.setRole(rs.getString("role"));
                list.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách tài khoản bị khóa: " + e.getMessage());
        }

        return list;
    }

    public boolean blockAccount(String userId) {
        String sql = "UPDATE Account SET is_block = 1 WHERE user_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi khóa tài khoản: " + e.getMessage());
            return false;
        }
    }

    public boolean unblockAccount(String userId) {
        String sql = "UPDATE Account SET is_block = 0 WHERE user_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi mở khóa tài khoản: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAccountByUserId(String userId) {
        String sql1 = "DELETE FROM Account WHERE user_id = ?";
        String sql2 = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DBUtils.getConnect()) {
            conn.setAutoCommit(false); // Bắt đầu transaction

            try (
                    PreparedStatement ps1 = conn.prepareStatement(sql1); PreparedStatement ps2 = conn.prepareStatement(sql2);) {
                ps1.setString(1, userId);
                ps1.executeUpdate();

                ps2.setString(1, userId);
                ps2.executeUpdate();

                conn.commit(); // Nếu cả hai thành công, commit
                return true;

            } catch (SQLException e) {
                conn.rollback(); // Lỗi thì rollback
                System.out.println("Lỗi khi xóa account/user: " + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối database: " + e.getMessage());
            return false;
        }
    }

    public String generateNewUserId() {
        String sql = "SELECT user_id FROM users ORDER BY user_id DESC LIMIT 1";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String lastId = rs.getString("user_id"); // CUS0012
                int num = Integer.parseInt(lastId.substring(3)) + 1;
                return String.format("CUS%04d", num);
            } else {
                return "CUS0001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String generateNewAccId() {
        String sql = "SELECT acc_id FROM account ORDER BY acc_id DESC LIMIT 1";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String lastId = rs.getString("acc_id"); // CUS0012
                int num = Integer.parseInt(lastId.substring(3)) + 1;
                return String.format("ACC%04d", num);
            } else {
                return "ACC0001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean createUserSkeleton(String userId, String role, String email) {
        String sql = "INSERT INTO users (user_id, role, email) VALUES (?, ?, ?)";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, role);  // Sử dụng tham số role truyền vào
            ps.setString(3, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createAccount(String accId, String password, String email, String userId) {
        String sql = "INSERT INTO Account (acc_id, password, email, regisDate, user_id, is_verified) VALUES (?, ?, ?, NOW(), ?, 1)";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accId);
            String passwordH = BCrypt.hashpw(password, BCrypt.gensalt());
            ps.setString(2, passwordH);
            ps.setString(3, email);
            ps.setString(4, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUserRole(String userId, String newRole) {
        String sql = "UPDATE users SET role = ? WHERE user_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newRole);
            ps.setString(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật vai trò người dùng: " + e.getMessage());
            return false;
        }
    }

    public double totalRevenue() {
        String sql = "SELECT SUM(total_amount) AS total_revenue FROM orders WHERE status IN ('paid', 'shipping', 'delivered')";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("total_revenue");
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Ghi log hoặc xử lý tùy hệ thống
        }
        return 0;
    }

    public List<CategoryStats> getTop6CategoryProductCounts() {
        List<CategoryStats> result = new ArrayList<>();
        String sql = "SELECT c.name AS category_name, COUNT(p.product_id) AS total "
                + "FROM categories c "
                + "LEFT JOIN product p ON c.category_id = p.category_id "
                + "GROUP BY c.name "
                + "ORDER BY total DESC "
                + "LIMIT 6";

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String categoryName = rs.getString("category_name");
                int total = rs.getInt("total");
                result.add(new CategoryStats(categoryName, total));
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Ghi log hoặc xử lý phù hợp hệ thống
        }

        return result;
    }

    public List<MonthRevenue> getMonthlyRevenue() {
        String sql = "SELECT MONTH(created_at) AS month, SUM(total_amount) AS total "
                + "FROM orders "
                + "WHERE status IN ('paid', 'shipping', 'delivered') "
                + "GROUP BY MONTH(created_at) "
                + "ORDER BY MONTH(created_at)";

        List<MonthRevenue> list = new ArrayList<>();

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new MonthRevenue(
                        rs.getInt("month"),
                        rs.getDouble("total")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Ghi log nếu cần
        }

        return list;
    }

    public static void main(String[] args) {
        AdminDAO dao = new AdminDAO();

        // Giả lập userId để test
        String testUserId = "CUS0004"; // thay bằng user_id có thật trong DB

        boolean result = dao.deleteAccountByUserId(testUserId);

        if (result) {
            System.out.println("xóa thành công cho user_id: " + testUserId);
        } else {
            System.out.println("Khóa tài khoản thất bại hoặc user không tồn tại.");
        }
    }

}
