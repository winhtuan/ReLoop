/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DAO;

import Model.Entity.ProductImage;
import java.sql.*;
import java.util.*;

public class ProductImageDao {
    private Connection conn;

    public ProductImageDao(Connection conn) {
        this.conn = conn;
    }

    // Lấy tất cả ảnh của một sản phẩm
    public List<ProductImage> getImagesByProductId(int productId) throws SQLException {
        List<ProductImage> images = new ArrayList<>();
        String sql = "SELECT * FROM product_images WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ProductImage image = new ProductImage();
                image.setId(rs.getInt("id"));
                image.setProductId(rs.getInt("product_id"));
                image.setImageUrl(rs.getString("image_url"));
                image.setIsPrimary(rs.getBoolean("is_primary"));
                images.add(image);
            }
        }
        return images;
    }

    // Lấy ảnh chính (is_primary = true) của một sản phẩm
    public ProductImage getPrimaryImage(int productId) throws SQLException {
        String sql = "SELECT * FROM product_images WHERE product_id = ? AND is_primary = 1";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ProductImage image = new ProductImage();
                image.setId(rs.getInt("id"));
                image.setProductId(rs.getInt("product_id"));
                image.setImageUrl(rs.getString("image_url"));
                image.setIsPrimary(true);
                return image;
            }
        }
        return null;
    }

    // Thêm ảnh mới
    public void insertImage(ProductImage image) throws SQLException {
        String sql = "INSERT INTO product_images (product_id, image_url, is_primary) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, image.getProductId());
            stmt.setString(2, image.getImageUrl());
            stmt.setBoolean(3, image.isIsPrimary());
            stmt.executeUpdate();
        }
    }

    // Xoá ảnh theo id
    public void deleteImageById(int imageId) throws SQLException {
        String sql = "DELETE FROM product_images WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, imageId);
            stmt.executeUpdate();
        }
    }

    // Xoá tất cả ảnh của một sản phẩm
    public void deleteImagesByProductId(int productId) throws SQLException {
        String sql = "DELETE FROM product_images WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.executeUpdate();
        }
    }
}

