package Model.DAO.admin;

import Model.entity.auth.User;
import Utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    public List<User> getListAccount() {
        String sql = "SELECT u.FullName, a.email, u.PhoneNumber, u.role FROM users u JOIN account a ON u.user_id = a.user_id WHERE a.is_block = 0 LIMIT 0, 1000;";

        List<User> list = new ArrayList<>();

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
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
        String sql = "SELECT u.FullName, a.email, u.PhoneNumber, u.role FROM Account a JOIN users u ON a.user_id = u.user_id WHERE a.isBlock = 1";

        List<User> list = new ArrayList<>();

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
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

    public boolean blockAccount(String accId) {
        String sql = "UPDATE Account SET isBlock = 1 WHERE acc_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi khóa tài khoản: " + e.getMessage());
            return false;
        }
    }

    public boolean unblockAccount(String accId) {
        String sql = "UPDATE Account SET isBlock = 0 WHERE acc_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi mở khóa tài khoản: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        AdminDAO adminDao = new AdminDAO();
        List<User> accounts = adminDao.getListAccount();

        if (accounts.isEmpty()) {
            System.out.println("Không có tài khoản nào chưa bị block.");
        } else {
            System.out.println("Danh sách tài khoản chưa bị block:");
            for (User user : accounts) {
                System.out.println("Tên: " + user.getFullName());
                System.out.println("Email: " + user.getEmail());
                System.out.println("Số điện thoại: " + user.getPhoneNumber());
                System.out.println("Role: " + user.getRole());
                System.out.println("-----------------------------");
            }
        }
    }
}