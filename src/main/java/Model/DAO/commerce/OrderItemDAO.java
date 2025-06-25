package Model.DAO.commerce;

import Model.entity.commerce.OrderItem;
import Utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderItemDAO {

    public void insert(OrderItem item) {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtils.getConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, item.getOrderId());
            ps.setString(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setInt(4, item.getPrice());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // hoặc log lỗi
        }
    }

    public void insertMany(List<OrderItem> items) {
        if (items == null || items.isEmpty()) return;

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

            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace(); // hoặc log lỗi
        }
    }
}
