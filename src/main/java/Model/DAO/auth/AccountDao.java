package Model.DAO.auth;

import Model.entity.auth.Account;
import Utils.DBUtils;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import org.mindrot.jbcrypt.BCrypt;

public class AccountDao {

    public String generateAccountId() {
        String sql = "SELECT acc_id FROM Account ORDER BY acc_id DESC LIMIT 1"; // MySQL dùng LIMIT 1
        String prefix = "ACC";
        int nextId = 1;

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("acc_id");
                int num = Integer.parseInt(lastId.substring(2));
                nextId = num + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return String.format("%s%04d", prefix, nextId); // Định dạng thành 'US00000X'
    }

    public Account checkLogin(String email, String password) throws SQLException {
        String sql = "SELECT * FROM Account WHERE email = ? AND is_verified = 1";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                LocalDate regisDate = null;
                Date date = rs.getDate("regisDate");
                if (date != null) {
                    regisDate = date.toLocalDate();
                }

                return new Account(
                        rs.getString("acc_id"),
                        rs.getString("password"),
                        rs.getString("email"),
                        regisDate,
                        rs.getString("user_id"),
                        rs.getString("verification_token"),
                        rs.getBoolean("is_verified")
                );
            }
        }
        return null;
    }

    public boolean addAccount(String email, String role, String userId) {
        String query = "INSERT INTO Account (password, email, role, regisDate, userid, is_verified, number_post) "
                + "VALUES (?, ?, ?, GETDATE(), ?, 1, 0)";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "google");
            ps.setString(2, email);
            ps.setString(3, role);
            ps.setString(4, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm tài khoản: " + e.getMessage());
        }
        return false;
    }

    public boolean updateToken(String email, String token) {
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

    public boolean verifyAccount(String token) {
        String query = "UPDATE Account SET is_verified = 1 WHERE verification_token = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, token);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi xác minh tài khoản: " + e);
        }
        return false;
    }

    public boolean isEmailExist(String email) {
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

    public String newAccount(Account account) {
        String id = generateAccountId(); // Tạo acc_id mới
        String sql = "INSERT INTO Account (acc_id, password, email, regisDate, userid, verification_token, is_verified, number_post) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBUtils.getConnect(); PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, id); // acc_id

            // Handle password
            if (account.getPassword() != null) {
                stmt.setString(2, account.getPassword());
            } else {
                stmt.setNull(2, java.sql.Types.VARCHAR);
            }

            // Handle email
            if (account.getEmail() != null) {
                stmt.setString(3, account.getEmail());
            } else {
                stmt.setNull(3, java.sql.Types.VARCHAR);
            }

            // Handle regisDate (dùng LocalDateTime và Timestamp)
            LocalDateTime regisDateTime;
            if (account.getRegisDate() != null) {
                // Giả sử getRegisDate() trả về LocalDate, convert sang LocalDateTime
                LocalDate regisDate = account.getRegisDate();
                regisDateTime = regisDate.atStartOfDay(); // Mặc định là 0 giờ nếu chỉ có ngày
            } else {
                regisDateTime = LocalDateTime.now();
            }

            stmt.setTimestamp(4, java.sql.Timestamp.valueOf(regisDateTime));

            // Handle userid
            stmt.setString(5, account.getUserId());

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

            // Thực thi lệnh INSERT
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Lỗi tạo tài khoản mới: " + ex.getMessage());
            id = null; // Trả về null nếu có lỗi
        }

        return id; // Trả về acc_id mới
    }

    public Account getAccountByEmail(String email) {
        String query = "SELECT * FROM Account WHERE email = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                LocalDate regisDate = null;
                Date date = rs.getDate("regisDate");
                if (date != null) {
                    regisDate = date.toLocalDate();
                }

                return new Account(
                        rs.getString("acc_id"),
                        rs.getString("password"),
                        rs.getString("email"),
                        regisDate,
                        rs.getString("user_id"),
                        rs.getString("verification_token"),
                        rs.getBoolean("is_verified")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving account by email: " + e.getMessage());
        }
        return null;
    }

    public void updatePassword(String email, String newPassword) {
        String query = "UPDATE Account SET password = ? WHERE email = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật mật khẩu: " + e);
        }
    }

    public List<Account> getAllAccounts() {
        List<Account> list = new ArrayList<>();
        String query = "SELECT * FROM Account";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LocalDate regisDate = null;
                Date date = rs.getDate("regisDate");
                if (date != null) {
                    regisDate = date.toLocalDate();
                }

                Account acc = new Account(
                        rs.getString("acc_id"),
                        null, // Password omitted for security reasons
                        rs.getString("email"),
                        regisDate,
                        rs.getString("user_id"),
                        rs.getString("verification_token"),
                        rs.getBoolean("is_verified")
                );
                list.add(acc);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving accounts: " + e.getMessage());
        }
        return list;
    }

    public String verifyResetToken(String token) throws SQLException {
        String sql = "SELECT email, expiryDate FROM PasswordResetToken WHERE token = ? AND expiryDate >= ?";
        LocalDateTime currentDateTime = LocalDateTime.now(); // 08:34 PM +07, 24/05/2025

        try (Connection con = DBUtils.getConnect(); PreparedStatement stmt = con.prepareStatement(sql)) {

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

    public void savePasswordResetToken(String email, String token, Date expiry) throws SQLException {
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
