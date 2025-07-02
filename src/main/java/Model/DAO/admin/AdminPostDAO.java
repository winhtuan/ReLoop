/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DAO.admin;

import Model.entity.post.Product;
import Model.entity.post.ProductImage;
import Model.entity.post.ProductReport;
import Utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class AdminPostDAO {

    public List<Product> approvalPost() {
        String sql = "SELECT product_id, user_id, title, description, price, moderation_status "
                + "FROM reloop_v2.product "
                + "WHERE moderation_status = 'pending'";
        List<Product> list = new ArrayList<>();
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getString("product_id"));
                product.setUserId(rs.getString("user_id"));
                product.setTitle(rs.getString("title"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price").intValue());
                product.setModerationStatus(rs.getString("moderation_status"));
                list.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách tài khoản chưa bị khóa: " + e.getMessage());
        }
        return list;
    }

    public List<ProductImage> image(String productId) {
        String sql = "SELECT image_url FROM reloop_v2.product_images WHERE product_id = ?";
        List<ProductImage> list = new ArrayList<>();

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, productId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProductImage productImage = new ProductImage();
                productImage.setImageUrl(rs.getString("image_url"));
                list.add(productImage);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách ảnh sản phẩm: " + e.getMessage());
        }

        return list;
    }

    public void deleteProductById(String productId) {
        String sql = "DELETE FROM reloop_v2.product WHERE product_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa sản phẩm: " + e.getMessage());
        }
    }

    public List<Product> getRejectPost() {
        String sql = "SELECT product_id, user_id, title, description, price, moderation_status "
                + "FROM reloop_v2.product "
                + "WHERE moderation_status = 'rejected'";
        List<Product> list = new ArrayList<>();
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getString("product_id"));
                product.setUserId(rs.getString("user_id"));
                product.setTitle(rs.getString("title"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price").intValue());
                product.setModerationStatus(rs.getString("moderation_status"));
                list.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách tài khoản chưa bị khóa: " + e.getMessage());
        }
        return list;
    }

    public List<Product> getapprovalList() {
        String sql = "SELECT product_id, user_id, title, description, price, moderation_status "
                + "FROM reloop_v2.product "
                + "WHERE moderation_status = 'approved'";
        List<Product> list = new ArrayList<>();
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getString("product_id"));
                product.setUserId(rs.getString("user_id"));
                product.setTitle(rs.getString("title"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price").intValue());
                product.setModerationStatus(rs.getString("moderation_status"));
                list.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách tài khoản chưa bị khóa: " + e.getMessage());
        }
        return list;
    }

    public void rejectPostById(String productId) {
        String sql = "UPDATE reloop_v2.product "
                + "SET moderation_status = 'rejected' "
                + "WHERE product_id = ? ";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật trạng thái sản phẩm: " + e.getMessage());
        }
    }

    public void pendingPostById(String productId) {
        String sql = "UPDATE reloop_v2.product "
                + "SET moderation_status = 'pending' "
                + "WHERE product_id = ? ";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật trạng thái sản phẩm: " + e.getMessage());
        }
    }

    public void approvePostById(String productId) {
        String sql = "UPDATE reloop_v2.product "
                + "SET moderation_status = 'approved' "
                + "WHERE product_id = ? ";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Lỗi khi duyệt bài đăng: " + e.getMessage());
        }
    }

    public void blockedpostbyid(String productId) {
        String sql = "UPDATE reloop_v2.product "
                + "SET moderation_status = 'blocked' "
                + "WHERE product_id = ? ";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Lỗi khi duyệt bài đăng: " + e.getMessage());
        }
    }

    public List<Product> getblockList() {
        String sql = "SELECT product_id, user_id, title, description, price, moderation_status "
                + "FROM reloop_v2.product "
                + "WHERE moderation_status = 'blocked'";
        List<Product> list = new ArrayList<>();
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getString("product_id"));
                product.setUserId(rs.getString("user_id"));
                product.setTitle(rs.getString("title"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price").intValue());
                product.setModerationStatus(rs.getString("moderation_status"));
                list.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách tài khoản chưa bị khóa: " + e.getMessage());
        }
        return list;
    }

    public int getTotalUsers() {
        String sql = "SELECT COUNT(*) AS total FROM reloop_v2.users";
        int total = 0;

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy tổng số người dùng: " + e.getMessage());
        }

        return total;
    }

    public int getTotalProducts() {
        String sql = "SELECT COUNT(*) AS total FROM reloop_v2.product";
        int total = 0;

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy tổng số sản phẩm: " + e.getMessage());
        }

        return total;
    }

    public int getTodayTotalProducts() {
        String sql = "SELECT COUNT(*) AS total FROM reloop_v2.product WHERE DATE(created_at) = CURDATE()";
        int total = 0;

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy tổng số sản phẩm hôm nay: " + e.getMessage());
        }

        return total;
    }

    public String getUserEmailByUserId(String userId) {
        String email = null;
        String sql = "SELECT email FROM reloop_v2.users WHERE user_id = ?";

        try (Connection conn = DBUtils.getConnect(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                email = rs.getString("email");
            }

        } catch (SQLException e) {
            System.err.println("Error fetching email for userId: " + userId);
            e.printStackTrace();
        }

        return email;
    }

    public List<ProductReport> getPendingReports() {
        List<ProductReport> list = new ArrayList<>();
        String sql = "SELECT report_id, product_id, reporter_id, reason, description, reported_at, status "
                + "FROM product_reports WHERE status = 'pending'";

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProductReport report = new ProductReport();
                report.setReportId(rs.getString("report_id"));
                report.setProductId(rs.getString("product_id"));
                report.setUserId(rs.getString("reporter_id"));
                report.setReason(rs.getString("reason"));
                report.setDescription(rs.getString("description"));
                report.setReportedAt(rs.getDate("reported_at"));
                report.setStatus(rs.getString("status"));

                list.add(report);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách báo cáo sản phẩm pending: " + e.getMessage());
        }

        return list;
    }

    public List<ProductReport> getActionTakenReports() {
        List<ProductReport> list = new ArrayList<>();
        String sql = "SELECT report_id, product_id, reporter_id, reason, description, reported_at, status "
                + "FROM product_reports WHERE status = 'action_taken'";

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProductReport report = new ProductReport();
                report.setReportId(rs.getString("report_id"));
                report.setProductId(rs.getString("product_id"));
                report.setUserId(rs.getString("reporter_id"));
                report.setReason(rs.getString("reason"));
                report.setDescription(rs.getString("description"));
                report.setReportedAt(rs.getDate("reported_at"));
                report.setStatus(rs.getString("status"));

                list.add(report);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách báo cáo sản phẩm action_taken: " + e.getMessage());
        }

        return list;
    }

    public boolean markReportAsActionTaken(String reportId) {
        String sql = "UPDATE product_reports SET status = 'action_taken' WHERE report_id = ? AND status = 'pending'";

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reportId);
            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0; // true nếu có ít nhất 1 dòng bị ảnh hưởng
        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật trạng thái báo cáo: " + e.getMessage());
            return false;
        }
    }

    public boolean revertReportToPending(String reportId) {
        String sql = "UPDATE product_reports SET status = 'pending' WHERE report_id = ? AND status = 'action_taken'";

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reportId);
            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi chuyển trạng thái báo cáo về pending: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteReportById(String reportId) {
        String sql = "DELETE FROM product_reports WHERE report_id = ?";

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reportId);
            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa báo cáo sản phẩm: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        AdminPostDAO dAO = new AdminPostDAO();
        String email = dAO.getUserEmailByUserId("CUS0014");

        if (email != null) {
            System.out.println("Email tìm được: " + email);
        } else {
            System.out.println("Không tìm thấy email cho user_id = Cus0014");
        }
    }

}
