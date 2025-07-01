package Model.DAO.post;

import java.util.Collections;
import Model.entity.post.Category;
import Utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY level, name";

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Category c = new Category();
                c.setCategoryId(rs.getInt("category_id"));
                c.setName(rs.getString("name"));
                int parent = rs.getInt("parent_id");
                c.setParentId(rs.wasNull() ? null : parent);
                c.setSlug(rs.getString("slug"));
                c.setLevel(rs.getInt("level"));
                categories.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Log properly in production
        }

        return categories;
    }

    public Category getCategoryBySlug(String slug) {
        String sql = "SELECT category_id, name, slug, parent_id, level FROM categories WHERE slug = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, slug);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Category(
                            rs.getInt("category_id"),
                            rs.getString("name"),
                            rs.getObject("parent_id") == null ? null : rs.getInt("parent_id"),
                            rs.getString("slug"),
                            rs.getInt("level")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Category> getSubCategories(int parentId) {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE parent_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, parentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Category c = new Category();
                    c.setCategoryId(rs.getInt("category_id"));
                    c.setName(rs.getString("name"));
                    c.setParentId(parentId);
                    c.setSlug(rs.getString("slug"));
                    c.setLevel(rs.getInt("level"));
                    list.add(c);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Integer> getAllSubCategoryIds(int parentId) {
        List<Integer> allSubCategoryIds = new ArrayList<>();
        allSubCategoryIds.add(parentId); // Thêm chính danh mục cha
        System.out.println("Starting getAllSubCategoryIds for parentId: " + parentId);
        getSubCategoryIdsRecursive(parentId, allSubCategoryIds);
        System.out.println("All sub-category IDs for parentId " + parentId + ": " + allSubCategoryIds);
        return allSubCategoryIds;
    }

    private void getSubCategoryIdsRecursive(int parentId, List<Integer> result) {
        String sql = "SELECT category_id FROM categories WHERE parent_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, parentId);
            System.out.println("Executing SQL: " + sql + " with parent_id: " + parentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int childId = rs.getInt("category_id");
                    System.out.println("Found sub-category ID: " + childId + " for parent_id: " + parentId);
                    result.add(childId); // Thêm danh mục con
                    getSubCategoryIdsRecursive(childId, result); // Gọi đệ quy để lấy danh mục cháu
                }
            }
        } catch (SQLException e) {
            System.err.println("SQLException in getSubCategoryIdsRecursive for parentId " + parentId + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Category> getCategoriesByParentId(Integer parentId) {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE parent_id " + (parentId == null ? "IS NULL" : "= ?") + " ORDER BY name";

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {

            if (parentId != null) {
                ps.setInt(1, parentId);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category c = new Category();
                c.setCategoryId(rs.getInt("category_id"));
                c.setName(rs.getString("name"));
                int pid = rs.getInt("parent_id");
                c.setParentId(rs.wasNull() ? null : pid);
                c.setSlug(rs.getString("slug"));
                c.setLevel(rs.getInt("level"));
                list.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Optional: get category by ID
    public Category getCategoryById(int id) {
        String sql = "SELECT * FROM categories WHERE category_id = ?";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Category(
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getObject("parent_id") == null ? null : rs.getInt("parent_id"),
                        rs.getString("slug"),
                        rs.getInt("level")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Category> getCategoriesByIds(List<Integer> categoryIds) {
        List<Category> categories = new ArrayList<>();
        if (categoryIds == null || categoryIds.isEmpty()) {
            return categories; // Trả về danh sách rỗng nếu không có ID
        }

        // Tạo câu truy vấn với IN clause
        String sql = "SELECT category_id, name, slug, parent_id, level FROM categories WHERE category_id IN ("
                + String.join(",", Collections.nCopies(categoryIds.size(), "?")) + ")";

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {

            // Đặt các tham số cho PreparedStatement
            for (int i = 0; i < categoryIds.size(); i++) {
                ps.setInt(i + 1, categoryIds.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getObject("parent_id") == null ? null : rs.getInt("parent_id"),
                        rs.getString("slug"),
                        rs.getInt("level")
                );
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public List<Category> getSubCategoriesByParentId(int parentId) {
        List<Category> subCategories = new ArrayList<>();
        String sql = "SELECT category_id, name, slug, parent_id, level FROM categories WHERE parent_id = ?";

        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, parentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Category category = new Category(
                            rs.getInt("category_id"),
                            rs.getString("name"),
                            rs.getObject("parent_id") == null ? null : rs.getInt("parent_id"),
                            rs.getString("slug"),
                            rs.getInt("level")
                    );
                    subCategories.add(category);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subCategories;
    }

    public List<Category> getAllLevelZeroCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT category_id, name, slug, parent_id, level FROM categories WHERE level = 0";
        try (Connection conn = DBUtils.getConnect(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getObject("parent_id") == null ? null : rs.getInt("parent_id"),
                        rs.getString("slug"),
                        rs.getInt("level")
                );
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

}
