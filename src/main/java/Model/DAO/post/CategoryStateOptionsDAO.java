package Model.DAO.post;

import Utils.DBUtils;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CategoryStateOptionsDAO {

    public Map<Integer, String[]> getAllCategoryStateOptions() {
        Map<Integer, String[]> categoryStateOptions = new HashMap<>();
        String sql = "SELECT category_id, options FROM category_state_options";
        try (Connection conn = DBUtils.getConnect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Gson gson = new Gson();
            while (rs.next()) {
                int categoryId = rs.getInt("category_id");
                String optionsJson = rs.getString("options");
                String[] options = gson.fromJson(optionsJson, String[].class);
                categoryStateOptions.put(categoryId, options);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryStateOptions;
    }
}