package Model.DAO.post;

import Utils.DBUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryAttributeDAO {

    private final Gson gson = new Gson();

    public Map<Integer, List<Map<String, Object>>> getAllCategoryAttributes() {
        Map<Integer, List<Map<String, Object>>> categoryAttributes = new HashMap<>();
        String sql = "SELECT category_id, attr_id, name, input_type, is_required, options FROM category_attribute";
        try (Connection conn = DBUtils.getConnect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int categoryId = rs.getInt("category_id");
                Map<String, Object> attr = new HashMap<>();
                attr.put("attr_id", rs.getInt("attr_id"));
                attr.put("name", rs.getString("name"));
                attr.put("input_type", rs.getString("input_type"));
                attr.put("is_required", rs.getBoolean("is_required"));
                
                // Parse options từ chuỗi JSON thành List<String>
                String optionsJson = rs.getString("options");
                if (optionsJson != null && !optionsJson.isEmpty()) {
                    List<String> options = gson.fromJson(optionsJson, new TypeToken<List<String>>(){}.getType());
                    attr.put("options", options);
                } else {
                    attr.put("options", null); // Nếu options là null hoặc rỗng
                }
                
                categoryAttributes.computeIfAbsent(categoryId, k -> new ArrayList<>()).add(attr);
                System.out.println("Fetched attribute for categoryId " + categoryId + ": " + attr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error in CategoryAttributeDAO: " + e.getMessage());
        }
        System.out.println("Total categoryAttributes fetched: " + categoryAttributes);
        return categoryAttributes;
    }
}