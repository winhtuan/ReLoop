/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DAO.commerce;

import Model.entity.post.Product;
import Model.entity.post.ProductImage;
import Utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Thanh Loc
 */
public class favoriteDAO {

    public boolean isFavorited(String userId, String productId) throws SQLException {
        String sql = "SELECT 1 FROM favorites WHERE user_id = ? AND product_id = ?";
        try (PreparedStatement stmt = DBUtils.getConnect().prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.setString(2, productId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public void addFavorite(String userId, String productId) throws SQLException {
        String sql = "INSERT INTO favorites (fav_id, user_id, product_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = DBUtils.getConnect().prepareStatement(sql)) {
            String favId = generateFavId();
            stmt.setString(1, favId);
            stmt.setString(2, userId);
            stmt.setString(3, productId);
            stmt.executeUpdate();
        }
    }

    public void removeFavorite(String userId, String productId) throws SQLException {
        String sql = "DELETE FROM favorites WHERE user_id = ? AND product_id = ?";
        try (PreparedStatement stmt = DBUtils.getConnect().prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.setString(2, productId);
            stmt.executeUpdate();
        }
    }

    private String generateFavId() throws SQLException {
        String prefix = "FAV";
        int random = (int) (Math.random() * 10000);
        return String.format("%s%04d", prefix, random);
    }

    public List<Product> getFavoriteProducts(String userId) {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT p.*, pi.img_id, pi.image_url, pi.is_primary FROM favorites f JOIN product p ON f.product_id = p.product_id LEFT JOIN product_images pi ON p.product_id = pi.product_id WHERE f.user_id = ?";

        try (Connection con = DBUtils.getConnect(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            Map<String, Product> productMap = new LinkedHashMap<>();

            while (rs.next()) {
                String pid = rs.getString("product_id");

                // Nếu sản phẩm chưa có trong map thì tạo mới
                Product product = productMap.get(pid);
                if (product == null) {
                    product = new Product(
                            pid,
                            rs.getString("user_id"),
                            rs.getInt("category_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getInt("price"),
                            rs.getString("location"),
                            rs.getString("status"),
                            rs.getBoolean("is_priority"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at")
                    );
                    product.setImages(new ArrayList<>());
                    productMap.put(pid, product);
                }

                // Thêm ảnh (nếu có)
                String imageUrl = rs.getString("image_url");
                if (imageUrl != null) {
                    ProductImage img = new ProductImage();
                    img.setImgId(rs.getInt("img_id"));
                    img.setProductId(pid);
                    img.setImageUrl(imageUrl);
                    img.setPrimary(rs.getBoolean("is_primary"));
                    product.getImages().add(img);
                }
            }

            products.addAll(productMap.values());

        } catch (SQLException e) {
            System.out.println("Error retrieving favorite products: " + e.getMessage());
        }

        return products;
    }

}
