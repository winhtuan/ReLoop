package Model.DAO.post;

import Model.entity.post.Product;
import Model.entity.post.ProductAttributeValue;
import Model.entity.post.ProductImage;
import Utils.DBUtils;
import java.util.Collections;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProductDao {

    private static final Logger LOGGER = Logger.getLogger(ProductDao.class.getName());

    public String generateProductId() {
        String sql = "SELECT product_id FROM product ORDER BY product_id DESC LIMIT 1"; // MySQL dùng LIMIT 1
        String prefix = "PROD";
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

        return String.format("%s%03d", prefix, nextId);
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
                        rs.getBigDecimal("price"),
                        rs.getString("location"),
                        rs.getString("status"),
                        rs.getBoolean("is_priority"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
                ProductImageDao imageDAO = new ProductImageDao(DBUtils.getConnect());
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
                        rs.getBigDecimal("price"),
                        rs.getString("location"),
                        rs.getString("status"),
                        rs.getBoolean("is_priority"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
                ProductImageDao imageDAO = new ProductImageDao(DBUtils.getConnect());
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
        String sql = "SELECT * FROM product WHERE title LIKE ? OR description LIKE ?";
        try (Connection con = DBUtils.getConnect(); PreparedStatement stmt = con.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product(
                        rs.getString("product_id"),
                        rs.getString("user_id"),
                        rs.getInt("category_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        rs.getString("location"),
                        rs.getString("status"),
                        rs.getBoolean("is_priority"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
                ProductImageDao imageDAO = new ProductImageDao(DBUtils.getConnect());
                product.setImages(imageDAO.getImagesByProductId(product.getProductId()));
                products.add(product);
            }
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
            stmt.setBigDecimal(6, product.getPrice());
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
            stmt.setBigDecimal(5, product.getPrice());
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

    public List<Product> getProductsByCategoryIds(List<Integer> categoryIds) {
        List<Product> products = new ArrayList<>();
        if (categoryIds == null || categoryIds.isEmpty()) {
            return products;
        }

        String sql = "SELECT * FROM product WHERE category_id IN ("
                + String.join(",", Collections.nCopies(categoryIds.size(), "?"))
                + ") AND moderation_status = 'approved' AND status = 'active' ORDER BY created_at DESC";

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < categoryIds.size(); i++) {
                ps.setInt(i + 1, categoryIds.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getString("product_id"));
                p.setTitle(rs.getString("title"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getBigDecimal("price"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                // Thêm các field khác tùy vào Product class bạn

                products.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> getProductsByCategoryIdsAndFilter(List<Integer> categoryIds, Double minPrice, Double maxPrice, String state) {
        List<Product> products = new ArrayList<>();
        if (categoryIds == null || categoryIds.isEmpty()) {
            return products;
        }

        StringBuilder sql = new StringBuilder("SELECT * FROM product WHERE category_id IN (");
        sql.append(categoryIds.stream().map(id -> "?").collect(Collectors.joining(",")));
        sql.append(") AND moderation_status = 'approved' AND status = 'active'");

        if (minPrice != null) {
            sql.append(" AND price >= ?");
        }
        if (maxPrice != null) {
            sql.append(" AND price <= ?");
        }
        if (state != null && !state.isEmpty()) {
            sql.append(" AND state = ?");
        }

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            for (Integer id : categoryIds) {
                ps.setInt(index++, id);
            }
            if (minPrice != null) {
                ps.setDouble(index++, minPrice);
            }
            if (maxPrice != null) {
                ps.setDouble(index++, maxPrice);
            }
            if (state != null && !state.isEmpty()) {
                ps.setString(index++, state);
            }

            ProductImageDao imageDAO = new ProductImageDao(conn); // Khởi tạo ProductImageDao

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setProductId(rs.getString("product_id"));
                    p.setTitle(rs.getString("title"));
                    p.setPrice(rs.getBigDecimal("price"));
                    p.setState(rs.getString("state"));
                    p.setLocation(rs.getString("location"));
                    p.setImages(imageDAO.getImagesByProductId(rs.getString("product_id")));
                    products.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public String saveProduct(Product product, List<ProductAttributeValue> attributeValues, List<ProductImage> images) throws SQLException {
        String generatedProductId = null;
        Connection conn = null;

        try {
            conn = DBUtils.getConnect();
            conn.setAutoCommit(false);

            // Lưu product
            String sqlProduct = "INSERT INTO product (user_id, category_id, title, description, price, location, state, status, is_priority, updated_at) " +
                               "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
            try (PreparedStatement ps = conn.prepareStatement(sqlProduct)) {
                LOGGER.info("Executing SQL: " + sqlProduct);
                LOGGER.info("Setting parameters: userId=" + product.getUserId() + ", categoryId=" + product.getCategoryId() +
                            ", title=" + product.getTitle() + ", description=" + product.getDescription() +
                            ", price=" + product.getPrice() + ", location=" + product.getLocation() +
                            ", state=" + product.getState() + ", status=" + product.getStatus() +
                            ", isPriority=" + product.isPriority());

                ps.setString(1, product.getUserId());
                ps.setInt(2, product.getCategoryId());
                ps.setString(3, product.getTitle());
                ps.setString(4, product.getDescription());
                ps.setBigDecimal(5, product.getPrice());
                ps.setString(6, product.getLocation());
                ps.setString(7, product.getState());
                ps.setString(8, product.getStatus());
                ps.setBoolean(9, product.isPriority());

                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating product failed, no rows affected.");
                }

                // Lấy product_id từ bảng product
                String sqlGetId = "SELECT product_id FROM product WHERE user_id = ? AND category_id = ? AND title = ? AND created_at = (SELECT MAX(created_at) FROM product WHERE user_id = ?)";
                try (PreparedStatement psGetId = conn.prepareStatement(sqlGetId)) {
                    psGetId.setString(1, product.getUserId());
                    psGetId.setInt(2, product.getCategoryId());
                    psGetId.setString(3, product.getTitle());
                    psGetId.setString(4, product.getUserId());
                    try (ResultSet rs = psGetId.executeQuery()) {
                        if (rs.next()) {
                            generatedProductId = rs.getString("product_id");
                            product.setProductId(generatedProductId);
                            LOGGER.info("Retrieved productId: " + generatedProductId);
                        } else {
                            throw new SQLException("Failed to retrieve generated product ID.");
                        }
                    }
                }
            }

            // Lưu attribute values
            if (attributeValues != null && !attributeValues.isEmpty()) {
                String sqlAttr = "INSERT INTO product_attribute_value (product_id, attr_id, value) VALUES (?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sqlAttr)) {
                    for (ProductAttributeValue attrValue : attributeValues) {
                        ps.setString(1, generatedProductId);
                        ps.setInt(2, attrValue.getAttributeId());
                        ps.setString(3, attrValue.getValue());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                    LOGGER.info("Inserted " + attributeValues.size() + " attribute values.");
                }
            }

            // Lưu ảnh với img_id tự động từ trigger
            if (images != null && !images.isEmpty()) {
                String sqlImage = "INSERT INTO product_images (product_id, image_url, is_primary) VALUES (?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sqlImage)) {
                    for (int i = 0; i < images.size(); i++) {
                        ProductImage image = images.get(i);
                        ps.setString(1, generatedProductId);
                        ps.setString(2, image.getImageUrl());
                        ps.setBoolean(3, i == 0); // Ảnh đầu tiên là primary
                        ps.addBatch();
                    }
                    ps.executeBatch();
                    LOGGER.info("Inserted " + images.size() + " images.");
                }
            }

            conn.commit();
            return generatedProductId;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    LOGGER.severe("Rollback successful due to: " + e.getMessage());
                } catch (SQLException rollbackEx) {
                    LOGGER.severe("Rollback failed: " + rollbackEx.getMessage());
                }
            }
            LOGGER.severe("SQL Error: " + e.getMessage());
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                    LOGGER.info("Connection closed.");
                } catch (SQLException e) {
                    LOGGER.severe("Failed to close connection: " + e.getMessage());
                }
            }
        }
    }
}
