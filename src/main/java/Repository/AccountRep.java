package Repository;

import Model.Account;
import Utils.DBUtils;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import org.mindrot.jbcrypt.BCrypt;

public class AccountRep {

    public static Account checkLogin(String email, String password) throws SQLException {
        String sql = "SELECT * FROM Account WHERE email = ? AND is_verified = 1";
        try (Connection con = DBUtils.getConnect(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (BCrypt.checkpw(password, hashedPassword)) {
                    return new Account(
                            rs.getInt("accID"),
                            hashedPassword,
                            rs.getString("email"),
                            rs.getString("role"),
                            rs.getDate("regisDate") != null ? rs.getDate("regisDate").toLocalDate() : null,
                            rs.getInt("userid"),
                            rs.getString("verification_token"),
                            rs.getBoolean("is_verified"),
                            rs.getInt("number_post")
                    );
                }
            }
        }
        return null;
    }

    public static boolean addAccount(String email, String role, int userId) {
        String query = "INSERT INTO Account (password, email, role, regisDate, userid, is_verified, number_post) "
                + "VALUES (?, ?, ?, GETDATE(), ?, 1, 0)";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "google");
            ps.setString(2, email);
            ps.setString(3, role);
            ps.setInt(4, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm tài khoản: " + e.getMessage());
        }
        return false;
    }

    public static boolean updateToken(String email, String token) {
        String query = "UPDATE Account SET verification_token = ? WHERE email = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, token);
            ps.setString(2, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật token xác minh: " + e);
        }
        return false;
    }

    public static boolean verifyAccount(String token) {
        String query = "UPDATE Account SET is_verified = 1 WHERE verification_token = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, token);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi xác minh tài khoản: " + e);
        }
        return false;
    }

    public static boolean isEmailExist(String email) {
        String sql = "SELECT accID FROM Account WHERE email = ?";
        try (Connection con = DBUtils.getConnect(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            System.out.println("Lỗi kiểm tra email tồn tại: " + ex);
        }
        return false;
    }

    public static int newAccount(Account account) {
    int id = -1;
    String sql = "INSERT INTO Account (password, email, role, regisDate, userid, verification_token, is_verified, number_post) "
            + "OUTPUT INSERTED.accID VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    try (Connection con = DBUtils.getConnect(); PreparedStatement stmt = con.prepareStatement(sql)) {
        // Handle password
        if (account.getPassword() != null) {
            stmt.setString(1, account.getPassword());
        } else {
            stmt.setNull(1, java.sql.Types.VARCHAR);
        }
        // Handle email
        if (account.getEmail() != null) {
            stmt.setString(2, account.getEmail());
        } else {
            stmt.setNull(2, java.sql.Types.VARCHAR);
        }
        // Handle role
        if (account.getRole() != null) {
            stmt.setString(3, account.getRole());
        } else {
            stmt.setNull(3, java.sql.Types.VARCHAR);
        }
        // Handle regisDate (now using LocalDateTime and java.sql.Timestamp)
        LocalDateTime regisDateTime;
        if (account.getRegisDate() != null) {
            // Assuming getRegisDate() returns a LocalDate for now, convert to LocalDateTime
            LocalDate regisDate = account.getRegisDate(); // Temporary assumption
            regisDateTime = regisDate.atStartOfDay(); // Default to midnight if only date is provided
        } else {
            // Use current date and time (May 24, 2025, 07:42 PM +07)
            regisDateTime = LocalDateTime.now(); // This will use the current system time
        }
        // Validate the date range for SQL Server datetime
        LocalDateTime minDateTime = LocalDateTime.of(1753, 1, 1, 0, 0);
        LocalDateTime maxDateTime = LocalDateTime.of(9999, 12, 31, 23, 59, 59);
        if (regisDateTime.isBefore(minDateTime) || regisDateTime.isAfter(maxDateTime)) {
            throw new IllegalArgumentException("regisDate is out of range for SQL Server datetime: " + regisDateTime);
        }
        // Convert LocalDateTime to java.sql.Timestamp for datetime column
        stmt.setTimestamp(4, java.sql.Timestamp.valueOf(regisDateTime));
        // Handle userid
        stmt.setInt(5, account.getUserId());
        // Handle verification_token
        if (account.getVerificationToken() != null) {
            stmt.setString(6, account.getVerificationToken());
        } else {
            stmt.setNull(6, java.sql.Types.VARCHAR);
        }
        // Handle is_verified
        stmt.setInt(7, account.isVerified() ? 1 : 0);
        // Handle number_post
        stmt.setInt(8, 0);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            id = rs.getInt(1);
        }
        rs.close();
    } catch (SQLException ex) {
        System.out.println("Lỗi tạo tài khoản mới: " + ex.getMessage());
    }
    return id;
}

    public static Account getAccountByEmail(String email) {
        String query = "SELECT * FROM Account WHERE email = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getInt("accID"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getDate("regisDate") != null ? rs.getDate("regisDate").toLocalDate() : null,
                        rs.getInt("userid"),
                        rs.getString("verification_token"),
                        rs.getBoolean("is_verified"),
                        rs.getInt("number_post")
                );
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy tài khoản theo email: " + e);
        }
        return null;
    }

    public static void updatePassword(String email, String newPassword) {
        String query = "UPDATE Account SET password = ? WHERE email = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật mật khẩu: " + e);
        }
    }

    public static List<Account> getAllAccounts() {
        List<Account> list = new ArrayList<>();
        String query = "SELECT * FROM Account";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Account acc = new Account(
                        rs.getInt("accID"),
                        null, // password lúc này truyền null hoặc lấy đúng vị trí nếu cần
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getDate("regisDate") != null ? rs.getDate("regisDate").toLocalDate() : null,
                        rs.getInt("userid"),
                        rs.getString("verification_token"),
                        rs.getBoolean("is_verified"),
                        rs.getInt("number_post")
                );
                list.add(acc);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách tài khoản: " + e);
        }
        return list;
    }

   public static String verifyResetToken(String token) throws SQLException {
        String sql = "SELECT email, expiryDate FROM PasswordResetToken WHERE token = ? AND expiryDate >= ?";
        LocalDateTime currentDateTime = LocalDateTime.now(); // 08:34 PM +07, 24/05/2025

        try (Connection con = DBUtils.getConnect(); 
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, token);
            stmt.setTimestamp(2, Timestamp.valueOf(currentDateTime)); // So sánh với thời gian hiện tại
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String email = rs.getString("email");
                Timestamp expiry = rs.getTimestamp("expiryDate");
                if (expiry != null && expiry.toLocalDateTime().isAfter(currentDateTime)) {
                    return email; // Trả về email nếu token hợp lệ
                }
            }
            throw new IllegalArgumentException("Token không hợp lệ hoặc đã hết hạn");
        }
    }
    
   public static void savePasswordResetToken(String email, String token, Date expiry) throws SQLException {
        String sql = "INSERT INTO PasswordResetToken (email, token, expiryDate) VALUES (?, ?, ?)";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, token);
            ps.setTimestamp(3, new Timestamp(expiry.getTime()));
            ps.executeUpdate();
        }
    }

    public static void main(String[] args) {
        System.out.println(BCrypt.hashpw("12345", BCrypt.gensalt()));
    }

}
