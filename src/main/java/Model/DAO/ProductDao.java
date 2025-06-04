/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DAO;

import Model.Entity.Product;
import Utils.DBUtils;
import java.sql.*;
import java.util.*;

public class ProductDao {

    public static Product getProductById(int productId) throws SQLException {
        String sql = "SELECT * FROM product WHERE id = ?";
        try (PreparedStatement stmt = DBUtils.getConnect().prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setUserId(rs.getInt("user_id"));
                product.setCategoryId(rs.getInt("category_id"));
                product.setTitle(rs.getString("title"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setLocation(rs.getString("location"));
                product.setStatus(rs.getString("status"));
                product.setCreatedAt(rs.getTimestamp("created_at"));
                product.setUpdatedAt(rs.getTimestamp("updated_at"));

                // Lấy hình ảnh liên quan
                ProductImageDao imageDAO = new ProductImageDao(DBUtils.getConnect());
                product.setImages(imageDAO.getImagesByProductId(productId));

                return product;
            }
        }
        return null;
    }

    public static List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM product";
        try (PreparedStatement stmt = DBUtils.getConnect().prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product();
                int productId = rs.getInt("id");

                product.setId(productId);
                product.setUserId(rs.getInt("user_id"));
                product.setCategoryId(rs.getInt("category_id"));
                product.setTitle(rs.getString("title"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setLocation(rs.getString("location"));
                product.setStatus(rs.getString("status"));
                product.setCreatedAt(rs.getTimestamp("created_at"));
                product.setUpdatedAt(rs.getTimestamp("updated_at"));

                ProductImageDao imageDAO = new ProductImageDao(DBUtils.getConnect());
                product.setImages(imageDAO.getImagesByProductId(productId));

                products.add(product);
            }
        }
        return products;
    }

    public static List<Product> getProductSearch(String kw) throws SQLException {
    List<Product> products = new ArrayList<>();
    String sql = "SELECT * FROM product WHERE title LIKE ? OR description LIKE ?";
    
    try (PreparedStatement stmt = DBUtils.getConnect().prepareStatement(sql)) {
        String keywordPattern = "%" + kw + "%";
        stmt.setString(1, keywordPattern);
        stmt.setString(2, keywordPattern);

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Product product = new Product();
                int productId = rs.getInt("id");

                product.setId(productId);
                product.setUserId(rs.getInt("user_id"));
                product.setCategoryId(rs.getInt("category_id"));
                product.setTitle(rs.getString("title"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setLocation(rs.getString("location"));
                product.setStatus(rs.getString("status"));
                product.setCreatedAt(rs.getTimestamp("created_at"));
                product.setUpdatedAt(rs.getTimestamp("updated_at"));

                ProductImageDao imageDAO = new ProductImageDao(DBUtils.getConnect());
                product.setImages(imageDAO.getImagesByProductId(productId));

                products.add(product);
            }
        }
    }

    return products;
}

    public static void main(String[] args) throws SQLException {
        for(Product a: getProductSearch("la"))
        {
            System.out.println(a.toString());
        }
    }
}
