package Model.DAO.commerce;

import Model.entity.commerce.OrderItem;
import Utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderItemDAO {

    public boolean insert(OrderItem item) {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtils.getConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, item.getOrderId());
            ps.setString(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setInt(4, item.getPrice());

            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Có lỗi thì trả về false
        }
    }

    public boolean insertMany(List<OrderItem> items) {
        if (items == null || items.isEmpty()) return false;

        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (OrderItem item : items) {
                ps.setString(1, item.getOrderId());
                ps.setString(2, item.getProductId());
                ps.setInt(3, item.getQuantity());
                ps.setInt(4, item.getPrice());
                ps.addBatch();
            }

            int[] results = ps.executeBatch();
            // Nếu batch nào cũng >0 thì true, ngược lại false
            for (int res : results) {
                if (res <= 0) return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
