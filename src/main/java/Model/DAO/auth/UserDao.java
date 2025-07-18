package Model.DAO.auth;

import java.sql.*;
import java.util.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import Model.entity.auth.User;
import Utils.DBUtils;

public class UserDao {

    public String generateUserId() {
        String sql = "SELECT user_id FROM users ORDER BY user_id DESC LIMIT 1"; // MySQL dùng LIMIT 1
        String prefix = "CUS";
        int nextId = 1; // Giá trị mặc định nếu bảng trống

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("user_id"); // Lấy user_id lớn nhất
                int num = Integer.parseInt(lastId.substring(3)); // Cắt bỏ 'US' để lấy số
                nextId = num + 1; // Tăng giá trị lên 1
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return String.format("%s%04d", prefix, nextId); // Định dạng thành 'US00000X'
    }

    public User getUserById(String userId) {
        User user = null;
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection con = DBUtils.getConnect(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User(
                        rs.getString("user_id"),
                        rs.getString("FullName"),
                        rs.getString("role"),
                        rs.getString("Address"),
                        rs.getString("PhoneNumber"),
                        rs.getString("email"),
                        rs.getBoolean("is_premium"),
                        rs.getTimestamp("premium_expiry"),
                        rs.getBigDecimal("balance"),
                        rs.getString("img")
                );
                System.out.println(rs.getString("img"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public String addUser(String name, String email) {
        String query = "INSERT INTO users (user_id, FullName, email, role) VALUES (?,?,?,'user')";
        String customerID = generateUserId(); // Giá trị mặc định nếu không lấy được ID

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(1, customerID);

            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet rs = ps.getGeneratedKeys(); // Lấy khóa chính được tạo tự động
                if (rs.next()) {
                    customerID = rs.getString("");
                    System.out.println("Thêm khách hàng thành công! ID: " + customerID);
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm khách hàng: " + e);
        }

        return customerID;
    }

    public boolean updatePremiumStatus(String userId, boolean isPremium, Date expiry) {
        String sql = "UPDATE users SET is_premium = ?, premium_expiry = ? WHERE user_id = ?";

        try (Connection con = DBUtils.getConnect(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, isPremium);
            ps.setObject(2, expiry);
            ps.setString(3, userId);

            int rows = ps.executeUpdate();
            return rows > 0; // true if at least one row was updated

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE users SET FullName=?, role=?, Address=?, PhoneNumber=?, email=?, is_premium=?, premium_expiry=?, balance=? WHERE user_id=?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getRole());
            ps.setString(3, user.getAddress());
            ps.setString(4, user.getPhoneNumber());
            ps.setString(5, user.getEmail());
            ps.setBoolean(6, user.isPremium());
            if (user.getPremiumExpiry() != null) {
                ps.setTimestamp(7, new Timestamp(user.getPremiumExpiry().getTime()));
            } else {
                ps.setNull(7, Types.TIMESTAMP);
            }
            ps.setBigDecimal(8, user.getBalance());
            ps.setString(9, user.getUserId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<User> listAllCustomers() {
        ArrayList<User> list = new ArrayList<>();
        try (Connection con = DBUtils.getConnect()) {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM users");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new User(
                        rs.getString("user_id"),
                        rs.getString("FullName"),
                        rs.getString("role"),
                        rs.getString("Address"),
                        rs.getString("PhoneNumber"),
                        rs.getString("email"),
                        rs.getBoolean("is_premium"),
                        rs.getTimestamp("premium_expiry"),
                        rs.getBigDecimal("balance")
                ));
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return list;
    }

    public static ArrayList<User> listAllCustomers(String uid) {
        AccountDao accDAO=new AccountDao();
        ArrayList<User> customers = new ArrayList<>();
        String sql = "SELECT u.user_id, u.FullName, u.Address, u.PhoneNumber, u.email "
                + "FROM conversation c "
                + "JOIN users u ON ((c.sender_id = ? AND u.user_id = c.receiver_id) OR "
                + "                 (c.receiver_id = ? AND u.user_id = c.sender_id))";

        try (PreparedStatement stmt = DBUtils.getConnect().prepareStatement(sql)) {
            stmt.setString(1, uid);
            stmt.setString(2, uid);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getString("user_id"));
                    user.setFullName(rs.getString("FullName"));
                    user.setAddress(rs.getString("Address"));
                    user.setPhoneNumber(rs.getString("PhoneNumber"));
                    user.setEmail(rs.getString("email"));
                    user.setAccount(accDAO.getAccountByEmail(user.getEmail()));
                    customers.add(user);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return customers;
    }

    public String newUser(User c) {
        String id = generateUserId();
        String sql = "INSERT INTO users (user_id, FullName, Address, PhoneNumber, email,role,img) "
                + "VALUES (?,?, ?, ?, ?, ?,?)";

        try (Connection con = DBUtils.getConnect(); PreparedStatement stmt = con.prepareStatement(sql)) {
            // Gán giá trị cho các tham số
            stmt.setString(1, c.getUserId());
            stmt.setString(2, c.getFullName());
            stmt.setString(3, c.getAddress());
            stmt.setString(4, c.getPhoneNumber());
            stmt.setString(5, c.getEmail());
            stmt.setString(6, c.getRole());
            stmt.setString(7, c.getSrcImg());

            // Thực thi truy vấn và lấy ID của bản ghi vừa thêm
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet rs = stmt.getGeneratedKeys(); // Lấy khóa chính được tạo tự động
                if (rs.next()) {
                    System.out.println("Thêm khách hàng thành công! ID: " + c.getUserId());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return id;
    }

    public boolean deleteCustomer(String user_id) {
        try (Connection conn = DBUtils.getConnect()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?");
            stmt.setString(1, user_id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Ghi log lỗi ra console
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println(new UserDao().generateUserId());
    }
}
