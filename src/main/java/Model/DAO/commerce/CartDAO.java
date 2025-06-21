/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DAO.commerce;

import Model.entity.post.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import Utils.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import Model.DAO.post.ProductImageDao;
import java.sql.SQLException;

/**
 *
 * @author Thanh Loc
 */
public class CartDAO {

    public String generateCartId() {
        String sql = "SELECT cart_id FROM cart ORDER BY cart_id DESC LIMIT 1";
        String prefix = "CRT";
        int nextId = 1;

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String lastId = rs.getString("cart_id");
                if (lastId != null && lastId.length() > prefix.length()) {
                    String numericPart = lastId.substring(prefix.length());
                    try {
                        int num = Integer.parseInt(numericPart);
                        nextId = num + 1;
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return String.format("%s%04d", prefix, nextId);
    }

    public List<Product> getCartProductsByUserId(String userId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, ci.quantity as cQuantity "
                + "FROM cart c "
                + "JOIN cart_items ci ON c.cart_id = ci.cart_id "
                + "JOIN product p ON ci.product_id = p.product_id "
                + "WHERE c.user_id = ?";

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setProductId(rs.getString("product_id"));
                    product.setUserId(rs.getString("user_id"));
                    product.setCategoryId(rs.getInt("category_id"));
                    product.setTitle(rs.getString("title"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getInt("price"));
                    product.setLocation(rs.getString("location"));
                    product.setStatus(rs.getString("status"));
                    product.setModerationStatus(rs.getString("moderation_status"));
                    product.setIsPriority(rs.getBoolean("is_priority"));
                    product.setCreatedAt(rs.getTimestamp("created_at"));
                    product.setState(rs.getString("state"));
                    product.setQuantity(rs.getInt("cQuantity")); // quantity của cartItem gán vào đây

                    // Gán ảnh nếu cần (giả sử có ProductDAO.getImagesByProductId)
                    product.setImages(new ProductImageDao().getImagesByProductId(product.getProductId()));

                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public void updateQuantity(String userId, String productId, int quantity) {
        String sql = "UPDATE cart_items "
                + "SET quantity = ? "
                + "WHERE cart_id = (SELECT cart_id FROM cart WHERE user_id = ?) "
                + "AND product_id = ?";

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setString(2, userId);
            ps.setString(3, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeItemFromCart(String userId, String productId) {
        String sql = "DELETE FROM cart_items WHERE cart_id = (SELECT cart_id FROM cart WHERE user_id = ?) AND product_id = ?";

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tổng tất cả quantity trong cart của user
     */
    /**
     * Đếm DISTINCT product_id trong cart của user
     */
    public int getTotalQuantityByUserId(String userId) {
        String sql = "SELECT COUNT(*) AS cnt FROM   cart c JOIN   cart_items ci ON c.cart_id = ci.cart_id WHERE  c.user_id = ?";

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getProductQuantityInCart(String userId, String productId) {
        String sql = "SELECT ci.quantity FROM cart c "
                + "JOIN cart_items ci ON c.cart_id = ci.cart_id "
                + "WHERE c.user_id = ? AND ci.product_id = ?";

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, productId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("quantity");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void addNewCart(String userId) {
        String sql = "INSERT INTO cart (cart_id, user_id)values(?,?)";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, generateCartId());
            ps.setString(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean hasCart(String userId) {
        String sql = "SELECT 1 FROM cart WHERE user_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // true nếu có cart
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addItemToCart(String userId, String productId, int quantity) {
        String sql = "INSERT INTO cart_items (cart_id, product_id, quantity) "
                + "VALUES ((SELECT cart_id FROM cart WHERE user_id = ?), ?, ?)";

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, productId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("ádas" + new CartDAO().hasCart("CUS0004"));
    }
}
