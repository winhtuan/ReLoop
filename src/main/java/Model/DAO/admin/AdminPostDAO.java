/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DAO.admin;

import Model.entity.post.Product;
import Model.entity.post.ProductImage;
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
                product.setPrice(rs.getBigDecimal("price"));
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
                product.setPrice(rs.getBigDecimal("price"));
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
                product.setPrice(rs.getBigDecimal("price"));
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
                product.setPrice(rs.getBigDecimal("price"));
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

}
