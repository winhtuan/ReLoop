package Model.DAO.post;

import Model.entity.post.Product;
import Model.entity.post.ProductImage;
import Utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ProductDao {

    public static List<Product> productList = null;

    public String generateProductId() {
        String sql = "SELECT product_id FROM product ORDER BY product_id DESC LIMIT 1"; // MySQL dùng LIMIT 1
        String prefix = "PRD";
        int nextId = 1;

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("product_id");
                int num = Integer.parseInt(lastId.substring(2));
                nextId = num + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return String.format("%s%04d", prefix, nextId);
    }

    public Product getProductById(String productId) {
        String sql = "SELECT * FROM product WHERE product_id = ?";
        try (Connection con = DBUtils.getConnect(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Product product = new Product(
                        rs.getString("product_id"),
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
                ProductImageDao imageDAO = new ProductImageDao();
                product.setImages(imageDAO.getImagesByProductId(productId));

                return product;
            }
        } catch (SQLException e) {
            System.out.println("Error getting product by ID: " + e.getMessage());
        }
        return null;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        if (productList != null) {
            return productList;
        }
        String sql = "SELECT * FROM product";
        try (Connection con = DBUtils.getConnect(); PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product(
                        rs.getString("product_id"),
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
                ProductImageDao imageDAO = new ProductImageDao();
                product.setImages(imageDAO.getImagesByProductId(product.getProductId()));
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving products: " + e.getMessage());
        }
        return products;
    }

    public List<Product> searchProducts(String keyword) {
        List<Product> products = new ArrayList<>();
        String sql = " SELECT p.*, pi.img_id, pi.image_url, pi.is_primary FROM product p LEFT JOIN product_images pi ON p.product_id = pi.product_id WHERE p.title LIKE ? OR p.description LIKE ? ";

        try (Connection con = DBUtils.getConnect(); PreparedStatement stmt = con.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            ResultSet rs = stmt.executeQuery();

            Map<String, Product> productMap = new LinkedHashMap<>();

            while (rs.next()) {
                String pid = rs.getString("product_id");
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
            System.out.println("Error searching products: " + e.getMessage());
        }

        return products;
    }

    public boolean addProduct(Product product) {
        String sql = "INSERT INTO product (product_id, user_id, category_id, title, description, price, location, status, is_priority, created_at, updated_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
        try (Connection con = DBUtils.getConnect(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, product.getProductId());
            stmt.setString(2, product.getUserId());
            if (product.getCategoryId() != null) {
                stmt.setInt(3, product.getCategoryId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setString(4, product.getTitle());
            stmt.setString(5, product.getDescription());
            stmt.setInt(6, product.getPrice());
            stmt.setString(7, product.getLocation());
            stmt.setString(8, product.getStatus());
            stmt.setBoolean(9, product.isPriority());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
        return false;
    }

    public boolean updateProduct(Product product) {
        String sql = "UPDATE product SET user_id = ?, category_id = ?, title = ?, description = ?, price = ?, location = ?, status = ?, is_priority = ?, updated_at = NOW() "
                + "WHERE product_id = ?";
        try (Connection con = DBUtils.getConnect(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, product.getUserId());
            if (product.getCategoryId() != null) {
                stmt.setInt(2, product.getCategoryId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setString(3, product.getTitle());
            stmt.setString(4, product.getDescription());
            stmt.setInt(5, product.getPrice());
            stmt.setString(6, product.getLocation());
            stmt.setString(7, product.getStatus());
            stmt.setBoolean(8, product.isPriority());
            stmt.setString(9, product.getProductId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteProduct(String productId) {
        String sql = "DELETE FROM product WHERE product_id = ?";
        try (Connection con = DBUtils.getConnect(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
        }
        return false;
    }

    public static void main(String[] args) {
        for (Product a : new ProductDao().searchProducts("s")) {
            System.out.println(a.getImages().get(0).getImageUrl());
        }
    }

}
