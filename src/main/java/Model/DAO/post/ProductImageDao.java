package Model.DAO.post;

import Model.entity.post.ProductImage;
import Utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductImageDao {

    // Get all images of a product
    public List<ProductImage> getImagesByProductId(String productId) throws SQLException {
        List<ProductImage> images = new ArrayList<>();
        String sql = "SELECT * FROM product_images WHERE product_id = ?";
        try (PreparedStatement stmt = DBUtils.getConnect().prepareStatement(sql)) {
            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ProductImage image = new ProductImage(
                        rs.getInt("img_id"),
                        rs.getString("product_id"),
                        rs.getString("image_url"),
                        rs.getBoolean("is_primary")
                );
                images.add(image);
            }
        }
        return images;
    }

    // Get primary image of a product
    public ProductImage getPrimaryImage(String productId) throws SQLException {
        String sql = "SELECT * FROM product_images WHERE product_id = ? AND is_primary = 1";
        try (PreparedStatement stmt = DBUtils.getConnect().prepareStatement(sql)) {
            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ProductImage(
                        rs.getInt("img_id"),
                        rs.getString("product_id"),
                        rs.getString("image_url"),
                        true
                );
            }
        }
        return null;
    }

    // Insert new image
    public boolean insertImage(ProductImage image) throws SQLException {
        String sql = "INSERT INTO product_images (product_id, image_url, is_primary) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = DBUtils.getConnect().prepareStatement(sql)) {
            stmt.setString(1, image.getProductId());
            stmt.setString(2, image.getImageUrl());
            stmt.setBoolean(3, image.isPrimary());
            return stmt.executeUpdate() > 0;
        }
    }

    // Delete image by ID
    public boolean deleteImageById(int imgId) throws SQLException {
        String sql = "DELETE FROM product_images WHERE img_id = ?";
        try (PreparedStatement stmt = DBUtils.getConnect().prepareStatement(sql)) {
            stmt.setInt(1, imgId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Delete all images of a product
    public boolean deleteImagesByProductId(String productId) throws SQLException {
        String sql = "DELETE FROM product_images WHERE product_id = ?";
        try (PreparedStatement stmt = DBUtils.getConnect().prepareStatement(sql)) {
            stmt.setString(1, productId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Example main method for testing
    public static void main(String[] args) throws SQLException {
        try (Connection conn = DBUtils.getConnect()) {
            ProductImageDao dao = new ProductImageDao();
            List<ProductImage> images = dao.getImagesByProductId("PRD0001");
            for (ProductImage img : images) {
                System.out.println(img.getImageUrl());
            }
        }
    }
}
