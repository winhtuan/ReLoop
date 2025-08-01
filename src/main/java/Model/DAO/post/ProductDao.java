package Model.DAO.post;

import Model.entity.post.CategoryAttribute;
import Model.entity.post.Product;
import Model.entity.post.ProductAttributeValue;
import Model.entity.post.ProductImage;
import Utils.DBUtils;
import java.util.Collections;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProductDao {

    private static final Logger LOGGER = Logger.getLogger(ProductDao.class.getName());
    public static List<Product> productList = null;

    public String generateProductId() {
        String sql = "SELECT product_id FROM product ORDER BY product_id DESC LIMIT 1";
        String prefix = "PRD";
        int nextId = 1; // Giá trị mặc định nếu bảng trống

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("product_id"); // Lấy user_id lớn nhất
                int num = Integer.parseInt(lastId.substring(3)); // Cắt bỏ 'US' để lấy số
                nextId = num + 1; // Tăng giá trị lên 1
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return String.format("%s%04d", prefix, nextId);
    }

    public int generateImageId() {
        String sql = "SELECT last_number FROM product_images_sequence WHERE id = 1";
        try (Connection con = DBUtils.getConnect(); PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("last_number");
            } else {
                // Initialize sequence if it doesn't exist
                String initSql = "INSERT INTO product_images_sequence (id, last_number) VALUES (1, 0)";
                try (PreparedStatement initStmt = con.prepareStatement(initSql)) {
                    initStmt.executeUpdate();
                }
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("Error generating image ID: " + e.getMessage());
        }
        return 0;
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
                        rs.getBigDecimal("price").intValue(),
                        rs.getString("location"),
                        rs.getString("status"),
                        rs.getBoolean("is_priority"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
                product.setState(rs.getString("state").trim()); // Ánh xạ state
                product.setQuantity(rs.getInt("quantity")); // Ánh xạ quantity
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
        // Nếu bạn vẫn muốn cache, giữ đoạn dưới (tuỳ chọn):
        if (productList != null) {
            return productList;
        }

        List<Product> products = new ArrayList<>();

        // JOIN luôn sang bảng product_images để gom ảnh trong một truy vấn duy nhất
        String sql = "SELECT p.*, pi.img_id, pi.image_url, pi.is_primary "
                + "FROM product p "
                + "LEFT JOIN product_images pi ON p.product_id = pi.product_id";

        try (Connection con = DBUtils.getConnect(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            // Sử dụng LinkedHashMap để giữ nguyên thứ tự insert
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
                            rs.getBigDecimal("price").intValue(),
                            rs.getString("location"),
                            rs.getString("status"),
                            rs.getBoolean("is_priority"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at")
                    );
                    product.setImages(new ArrayList<>()); // chuẩn bị list ảnh
                    productMap.put(pid, product);
                }

                // Thêm ảnh (nếu có) vào danh sách ảnh của sản phẩm
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
            // Lưu vào cache nếu cần
            productList = products;

        } catch (SQLException e) {
            System.out.println("Error retrieving products: " + e.getMessage());
        }

        return products;
    }

    public int countAllProducts() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM product";
        try (Connection con = DBUtils.getConnect(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<Product> getListProductInOrderItem(String orderId) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.* FROM order_items oi JOIN product p ON oi.product_id = p.product_id WHERE oi.order_id = ?";

        try (Connection con = DBUtils.getConnect(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setProductId(rs.getString("product_id"));
                    p.setUserId(rs.getString("user_id"));
                    p.setCategoryId(rs.getInt("category_id"));
                    p.setTitle(rs.getString("title"));
                    p.setDescription(rs.getString("description"));
                    p.setPrice(rs.getInt("price")); // hoặc rs.getBigDecimal("price") nếu dùng BigDecimal
                    p.setLocation(rs.getString("location"));
                    p.setStatus(rs.getString("status"));
                    p.setModerationStatus(rs.getString("moderation_status"));
                    p.setIsPriority(rs.getBoolean("is_priority"));
                    p.setCreatedAt(rs.getTimestamp("created_at"));
                    p.setState(rs.getString("state"));
                    p.setQuantity(rs.getInt("quantity"));
                    ProductImageDao imageDAO = new ProductImageDao();
                    p.setImages(imageDAO.getImagesByProductId(p.getProductId()));
                    list.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Product> searchProducts(String keyword) {
        List<Product> products = new ArrayList<>();
        String sql = " SELECT p.*, pi.img_id, pi.image_url, pi.is_primary FROM product p LEFT JOIN product_images pi ON p.product_id = pi.product_id WHERE p.title LIKE ?";

        try (Connection con = DBUtils.getConnect(); PreparedStatement stmt = con.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
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
                            rs.getBigDecimal("price").intValue(), // 🔴 chuyển BigDecimal → int
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
            stmt.setBoolean(9, product.isIsPriority());
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
            stmt.setBoolean(8, product.isIsPriority());
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
                p.setPrice(rs.getInt("price"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                // Thêm các field khác tùy vào Product class bạn

                products.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> getProductsByCategoryIdsAndFilter(List<Integer> categoryIds, Double minPrice, Double maxPrice, String state, Map<Integer, String> attributeFilters) {
        List<Product> products = new ArrayList<>();
        if (categoryIds == null || categoryIds.isEmpty()) {
            return products;
        }

        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT p.* "
                + "FROM product p "
                + "LEFT JOIN product_attribute_value pav ON p.product_id = pav.product_id "
                + "WHERE p.category_id IN ("
        );
        sql.append(categoryIds.stream().map(id -> "?").collect(Collectors.joining(",")));
        sql.append(") AND p.moderation_status = 'approved' AND p.status = 'active'");

        if (minPrice != null) {
            sql.append(" AND p.price >= ?");
        }
        if (maxPrice != null) {
            sql.append(" AND p.price <= ?");
        }
        if (state != null && !state.isEmpty()) {
            sql.append(" AND p.state = ?");
        }
        if (attributeFilters != null && !attributeFilters.isEmpty()) {
            for (Integer attrId : attributeFilters.keySet()) {
                sql.append(" AND EXISTS (SELECT 1 FROM product_attribute_value pav2 WHERE pav2.product_id = p.product_id AND pav2.attr_id = ? AND pav2.value = ?)");
            }
        }
        sql.append(" ORDER BY p.created_at DESC");

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
            if (attributeFilters != null && !attributeFilters.isEmpty()) {
                for (Map.Entry<Integer, String> entry : attributeFilters.entrySet()) {
                    ps.setInt(index++, entry.getKey());
                    ps.setString(index++, entry.getValue());
                }
            }

            ProductImageDao imageDAO = new ProductImageDao();

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setProductId(rs.getString("product_id"));
                    p.setTitle(rs.getString("title"));
                    p.setDescription(rs.getString("description"));
                    p.setPrice(rs.getInt("price"));
                    p.setState(rs.getString("state"));
                    p.setLocation(rs.getString("location"));
                    p.setCreatedAt(rs.getTimestamp("created_at"));
                    p.setImages(imageDAO.getImagesByProductId(rs.getString("product_id")));
                    products.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public String saveProduct(Product product, List<ProductAttributeValue> attributeValues, List<ProductImage> images, String moderationStatus) throws SQLException {
        Connection conn = null;

        try {
            conn = DBUtils.getConnect();
            if (conn == null) {
                throw new SQLException("Failed to establish database connection");
            }
            conn.setAutoCommit(false);

            // Generate product_id first
            String productId = generateProductId();
            if (productId == null) {
                throw new SQLException("Failed to generate product ID");
            }
            product.setProductId(productId);
            LOGGER.info("Generated productId: " + productId);

            // Lưu product với product_id
            String sqlProduct = "INSERT INTO product (product_id, user_id, category_id, title, description, price, location, state, status, moderation_status, is_priority, created_at, updated_at) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
            try (PreparedStatement ps = conn.prepareStatement(sqlProduct)) {
                LOGGER.info("Executing SQL: " + sqlProduct);
                LOGGER.info("Setting parameters: productId=" + productId + ", userId=" + product.getUserId() + ", categoryId=" + product.getCategoryId()
                        + ", title=" + product.getTitle() + ", description=" + product.getDescription()
                        + ", price=" + product.getPrice() + ", location=" + product.getLocation()
                        + ", state=" + product.getState() + ", status=" + product.getStatus()
                        + ", moderationStatus=" + moderationStatus + ", isPriority=" + product.isIsPriority());

                ps.setString(1, productId);
                ps.setString(2, product.getUserId());
                ps.setInt(3, product.getCategoryId());
                ps.setString(4, product.getTitle());
                ps.setString(5, product.getDescription());
                ps.setInt(6, product.getPrice());
                ps.setString(7, product.getLocation());
                ps.setString(8, product.getState());
                ps.setString(9, product.getStatus() != null ? product.getStatus() : "active");
                ps.setString(10, moderationStatus);
                ps.setBoolean(11, product.isIsPriority());

                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating product failed, no rows affected.");
                }
                LOGGER.info("Product inserted successfully with ID: " + productId);
            }

            // Lưu attribute values
            if (attributeValues != null && !attributeValues.isEmpty()) {
                String sqlAttr = "INSERT INTO product_attribute_value (product_id, attr_id, value) VALUES (?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sqlAttr)) {
                    for (ProductAttributeValue attrValue : attributeValues) {
                        if (attrValue.getValue() != null && !attrValue.getValue().trim().isEmpty()) {
                            ps.setString(1, productId);
                            ps.setInt(2, attrValue.getAttributeId());
                            ps.setString(3, attrValue.getValue().trim());
                            ps.addBatch();
                        }
                    }
                    int[] results = ps.executeBatch();
                    LOGGER.info("Inserted " + results.length + " attribute values.");
                }
            }

            // Lưu ảnh với img_id tự động từ trigger
            if (images != null && !images.isEmpty()) {
                // Check if sequence exists, if not initialize it
                String checkSequenceSql = "SELECT COUNT(*) FROM product_images_sequence WHERE id = 1";
                boolean sequenceExists = false;
                try (PreparedStatement psCheckSequence = conn.prepareStatement(checkSequenceSql)) {
                    ResultSet rs = psCheckSequence.executeQuery();
                    if (rs.next()) {
                        sequenceExists = rs.getInt(1) > 0;
                    }
                }
                
                if (!sequenceExists) {
                    // Initialize sequence
                    String initSequenceSql = "INSERT INTO product_images_sequence (id, last_number) VALUES (1, 0)";
                    try (PreparedStatement psInitSequence = conn.prepareStatement(initSequenceSql)) {
                        psInitSequence.executeUpdate();
                    }
                }
                
                String sqlImage = "INSERT INTO product_images (product_id, image_url, is_primary) VALUES (?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sqlImage)) {
                    for (int i = 0; i < images.size(); i++) {
                        ProductImage image = images.get(i);
                        if (image.getImageUrl() != null && !image.getImageUrl().trim().isEmpty()) {
                            ps.setString(1, productId);
                            ps.setString(2, image.getImageUrl().trim());
                            ps.setBoolean(3, i == 0); // Ảnh đầu tiên là primary
                            ps.addBatch();
                        }
                    }
                    int[] results = ps.executeBatch();
                    LOGGER.info("Inserted " + results.length + " images.");
                }
            }

            // Update sequence after successful insert
            String updateSequenceSql = "UPDATE product_sequence SET last_number = last_number + 1 WHERE id = 1";
            try (PreparedStatement psUpdateSequence = conn.prepareStatement(updateSequenceSql)) {
                int affectedRows = psUpdateSequence.executeUpdate();
                if (affectedRows == 0) {
                    // If no rows affected, insert new sequence record
                    String insertSequenceSql = "INSERT INTO product_sequence (id, last_number) VALUES (1, 2)";
                    try (PreparedStatement psInsertSequence = conn.prepareStatement(insertSequenceSql)) {
                        psInsertSequence.executeUpdate();
                    }
                }
                LOGGER.info("Sequence updated successfully");
            }

            conn.commit();
            return productId;
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

    public List<Product> getProductsByUserId(String userId) {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT p.*, pi.img_id, pi.image_url, pi.is_primary "
                + "FROM product p "
                + "LEFT JOIN product_images pi ON p.product_id = pi.product_id "
                + "WHERE p.user_id = ?";

        try (Connection con = DBUtils.getConnect(); PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            Map<String, Product> productMap = new LinkedHashMap<>();

            while (rs.next()) {
                String pid = rs.getString("product_id");
                Product product = productMap.get(pid);

                if (product == null) {
                    product = new Product(
                            pid,
                            userId,
                            rs.getInt("category_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getBigDecimal("price").intValue(),
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
            System.out.println("Error retrieving products by user ID: " + e.getMessage());
        }

        return products;
    }

    public List<CategoryAttribute> getCategoryAttributesByCategoryId(Integer categoryId) {
        List<CategoryAttribute> categoryAttributes = new ArrayList<>();
        if (categoryId == null) {
            LOGGER.warning("categoryId is null in getCategoryAttributesByCategoryId");
            return categoryAttributes;
        }
        String sql = "SELECT attr_id, category_id, name, input_type, options, is_required FROM category_attribute WHERE category_id = ?";

        try (Connection con = DBUtils.getConnect(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CategoryAttribute attr = new CategoryAttribute();
                    attr.setAttributeId(rs.getInt("attr_id"));
                    attr.setCategoryId(rs.getInt("category_id"));
                    attr.setName(rs.getString("name"));
                    attr.setInputType(rs.getString("input_type")); // Ánh xạ đúng cột input_type
                    attr.setOptions(rs.getString("options"));
                    attr.setRequired(rs.getBoolean("is_required"));
                    categoryAttributes.add(attr);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error retrieving category attributes by categoryId " + categoryId + ": " + e.getMessage());
        }

        return categoryAttributes;
    }

    public List<ProductAttributeValue> getAttributeValuesByProductId(String productId) {
        List<ProductAttributeValue> attributeValues = new ArrayList<>();
        String sql = "SELECT product_id, attr_id, value FROM product_attribute_value WHERE product_id = ?";

        try (Connection con = DBUtils.getConnect(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ProductAttributeValue attrVal = new ProductAttributeValue();
                    attrVal.setProductId(rs.getString("product_id"));
                    attrVal.setAttributeId(rs.getInt("attr_id")); // Sửa tên cột
                    attrVal.setValue(rs.getString("value"));
                    attributeValues.add(attrVal);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error retrieving attribute values by productId: " + e.getMessage());
        }

        return attributeValues;
    }

    public List<ProductImage> getProductImagesByProductId(String productId) {
        List<ProductImage> images = new ArrayList<>();
        String sql = "SELECT img_id, product_id, image_url, is_primary FROM product_images WHERE product_id = ?";

        try (Connection con = DBUtils.getConnect(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProductImage image = new ProductImage();
                image.setImgId(rs.getInt("img_id"));
                image.setProductId(rs.getString("product_id"));
                image.setImageUrl(rs.getString("image_url"));
                image.setPrimary(rs.getBoolean("is_primary"));
                images.add(image);
            }
        } catch (SQLException e) {
            LOGGER.severe("Error retrieving product images by productId: " + e.getMessage());
        }

        return images;
    }

    public String updateProduct(Product product, List<ProductAttributeValue> attributeValues, List<ProductImage> images) {
        Connection conn = null;
        String productId = product.getProductId();

        try {
            conn = DBUtils.getConnect();
            conn.setAutoCommit(false);

            // Lấy thông tin sản phẩm hiện tại để so sánh
            Product currentProduct = getProductById(productId);
            if (currentProduct == null) {
                throw new SQLException("Product not found with productId: " + productId);
            }

            // Xây dựng câu SQL động để cập nhật chỉ các trường thay đổi
            StringBuilder sqlProduct = new StringBuilder("UPDATE product SET updated_at = NOW()");
            List<Object> params = new ArrayList<>();
            if (!Objects.equals(product.getUserId(), currentProduct.getUserId())) {
                sqlProduct.append(", user_id = ?");
                params.add(product.getUserId());
            }
            if (product.getCategoryId() != null && !product.getCategoryId().equals(currentProduct.getCategoryId())) {
                sqlProduct.append(", category_id = ?");
                params.add(product.getCategoryId());
            }
            if (!Objects.equals(product.getTitle(), currentProduct.getTitle())) {
                sqlProduct.append(", title = ?");
                params.add(product.getTitle());
            }
            if (!Objects.equals(product.getDescription(), currentProduct.getDescription())) {
                sqlProduct.append(", description = ?");
                params.add(product.getDescription());
            }
            if (product.getPrice() != currentProduct.getPrice()) {
                sqlProduct.append(", price = ?");
                params.add(product.getPrice());
            }
            if (!Objects.equals(product.getLocation(), currentProduct.getLocation())) {
                sqlProduct.append(", location = ?");
                params.add(product.getLocation());
            }
            if (!Objects.equals(product.getState(), currentProduct.getState())) {
                sqlProduct.append(", state = ?");
                params.add(product.getState());
            }
            if (!Objects.equals(product.getStatus(), currentProduct.getStatus())) {
                sqlProduct.append(", status = ?");
                params.add(product.getStatus() != null ? product.getStatus() : "active"); // Mặc định "active" nếu null
            }
            if (product.isIsPriority() != currentProduct.isIsPriority()) {
                sqlProduct.append(", is_priority = ?");
                params.add(product.isIsPriority());
            }
            if (product.getQuantity() != currentProduct.getQuantity()) {
                sqlProduct.append(", quantity = ?");
                params.add(product.getQuantity());
            }
            if (!Objects.equals(product.getModerationStatus(), currentProduct.getModerationStatus())) {
                sqlProduct.append(", moderation_status = ?");
                params.add(product.getModerationStatus() != null ? product.getModerationStatus() : "");
            }

            sqlProduct.append(" WHERE product_id = ?");
            params.add(productId);

            // Thực thi cập nhật sản phẩm
            try (PreparedStatement ps = conn.prepareStatement(sqlProduct.toString())) {
                for (int i = 0; i < params.size(); i++) {
                    if (params.get(i) instanceof Integer) {
                        ps.setInt(i + 1, (Integer) params.get(i));
                    } else if (params.get(i) instanceof String) {
                        ps.setString(i + 1, (String) params.get(i));
                    } else if (params.get(i) instanceof Boolean) {
                        ps.setBoolean(i + 1, (Boolean) params.get(i));
                    }
                }
                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    LOGGER.warning("No rows updated for productId: " + productId + ". Possible no changes or invalid data.");
                }
            }

            // Xử lý attributeValues
            List<ProductAttributeValue> currentAttrs = getAttributeValuesByProductId(productId) != null ? getAttributeValuesByProductId(productId) : new ArrayList<>();
            if (attributeValues != null) {
                for (ProductAttributeValue currentAttr : currentAttrs) {
                    boolean found = false;
                    for (ProductAttributeValue newAttr : attributeValues) {
                        if (currentAttr.getAttributeId() == newAttr.getAttributeId()
                                && Objects.equals(currentAttr.getValue(), newAttr.getValue())) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        String deleteAttrSql = "DELETE FROM product_attribute_value WHERE product_id = ? AND attr_id = ?";
                        try (PreparedStatement psDeleteAttr = conn.prepareStatement(deleteAttrSql)) {
                            psDeleteAttr.setString(1, productId);
                            psDeleteAttr.setInt(2, currentAttr.getAttributeId());
                            psDeleteAttr.executeUpdate();
                        }
                    }
                }
                for (ProductAttributeValue newAttr : attributeValues) {
                    boolean found = false;
                    for (ProductAttributeValue currentAttr : currentAttrs) {
                        if (currentAttr.getAttributeId() == newAttr.getAttributeId()
                                && Objects.equals(currentAttr.getValue(), newAttr.getValue())) {
                            found = true;
                            break;
                        }
                    }
                    if (!found && newAttr.getValue() != null && !newAttr.getValue().isEmpty()) {
                        String insertAttrSql = "INSERT INTO product_attribute_value (product_id, attr_id, value) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE value = ?";
                        try (PreparedStatement psInsertAttr = conn.prepareStatement(insertAttrSql)) {
                            psInsertAttr.setString(1, productId);
                            psInsertAttr.setInt(2, newAttr.getAttributeId());
                            psInsertAttr.setString(3, newAttr.getValue());
                            psInsertAttr.setString(4, newAttr.getValue());
                            psInsertAttr.executeUpdate();
                        }
                    }
                }
            }

            // Xử lý images
            List<ProductImage> currentImages = getProductImagesByProductId(productId) != null ? getProductImagesByProductId(productId) : new ArrayList<>();
            if (images != null) {
                for (ProductImage currentImage : currentImages) {
                    boolean found = false;
                    for (ProductImage newImage : images) {
                        if (currentImage.getImageUrl() != null && currentImage.getImageUrl().equals(newImage.getImageUrl())) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        String deleteImageSql = "DELETE FROM product_images WHERE product_id = ? AND image_url = ?";
                        try (PreparedStatement psDeleteImage = conn.prepareStatement(deleteImageSql)) {
                            psDeleteImage.setString(1, productId);
                            psDeleteImage.setString(2, currentImage.getImageUrl());
                            psDeleteImage.executeUpdate();
                        }
                    }
                }
                for (ProductImage newImage : images) {
                    boolean found = false;
                    for (ProductImage currentImage : currentImages) {
                        if (currentImage.getImageUrl() != null && currentImage.getImageUrl().equals(newImage.getImageUrl())) {
                            found = true;
                            break;
                        }
                    }
                    if (!found && newImage.getImageUrl() != null) {
                        // Generate img_id first - handle case where sequence doesn't exist
                        String checkSequenceSql = "SELECT COUNT(*) FROM product_images_sequence WHERE id = 1";
                        boolean sequenceExists = false;
                        try (PreparedStatement psCheckSequence = conn.prepareStatement(checkSequenceSql)) {
                            ResultSet rs = psCheckSequence.executeQuery();
                            if (rs.next()) {
                                sequenceExists = rs.getInt(1) > 0;
                            }
                        }
                        
                        if (!sequenceExists) {
                            // Initialize sequence
                            String initSequenceSql = "INSERT INTO product_images_sequence (id, last_number) VALUES (1, 0)";
                            try (PreparedStatement psInitSequence = conn.prepareStatement(initSequenceSql)) {
                                psInitSequence.executeUpdate();
                            }
                        }
                        
                        String updateSequenceSql = "UPDATE product_images_sequence SET last_number = last_number + 1 WHERE id = 1";
                        try (PreparedStatement psUpdateSequence = conn.prepareStatement(updateSequenceSql)) {
                            psUpdateSequence.executeUpdate();
                        }
                        
                        String getImgIdSql = "SELECT last_number FROM product_images_sequence WHERE id = 1";
                        int imgId = 0;
                        try (PreparedStatement psGetImgId = conn.prepareStatement(getImgIdSql)) {
                            ResultSet rs = psGetImgId.executeQuery();
                            if (rs.next()) {
                                imgId = rs.getInt("last_number");
                            }
                        }
                        
                        String insertImageSql = "INSERT INTO product_images (img_id, product_id, image_url, is_primary) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement psInsertImage = conn.prepareStatement(insertImageSql)) {
                            psInsertImage.setInt(1, imgId);
                            psInsertImage.setString(2, productId);
                            psInsertImage.setString(3, newImage.getImageUrl());
                            psInsertImage.setBoolean(4, images.indexOf(newImage) == 0);
                            psInsertImage.executeUpdate();
                        }
                    }
                }
            }

            conn.commit();
            LOGGER.info("Product updated successfully with productId: " + productId);
            return productId;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    LOGGER.severe("Rollback successful due to: " + e.getMessage());
                } catch (SQLException rollbackEx) {
                    LOGGER.severe("Rollback failed: " + rollbackEx.getMessage());
                }
            }
            LOGGER.log(Level.SEVERE, "Error updating product: " + e.getMessage(), e);
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Failed to close connection: " + e.getMessage(), e);
                }
            }
        }
    }
}
