package Service;

import Model.DAO.auth.UserDao;
import Model.DAO.commerce.OrderDao;
import Model.DAO.commerce.OrderItemDAO;
import Model.DAO.post.ProductDao;
import Model.entity.commerce.Order;
import Model.entity.commerce.OrderItem;
import Model.entity.post.Product;

import java.util.*;

public class OrderService {

    // Save list of orders and their items to the database
    public boolean saveOrdersWithItems(List<Order> orders, String buyerId) {
        try {
            for (Order order : orders) {
                int totalPrice = order.getListItems()
                        .stream()
                        .mapToInt(item -> item.getPrice() * item.getQuantity())
                        .sum();
                order.setTotalAmount(totalPrice);
                order.setOrderId(new OrderDao().generateOrderId());
                order.setUserId(buyerId);
                boolean orderInserted = new OrderDao().insert(order); // Sửa lại insert trả về boolean
                if (!orderInserted) {
                    return false;
                }

                for (OrderItem item : order.getListItems()) {
                    item.setOrderId(order.getOrderId());
                    item.setPrice(item.getPrice() * item.getQuantity());
                    boolean itemInserted = new OrderItemDAO().insert(item); // Sửa lại insert trả về boolean
                    if (!itemInserted) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Group cart items by sellerId
    public Map<String, List<OrderItem>> groupItemsBySeller(String[] productIds, Map<String, Integer> productQuantities) {
        ProductDao pDao = new ProductDao();
        UserDao userDao = new UserDao();
        Map<String, List<OrderItem>> bySeller = new HashMap<>();

        for (String pid : productIds) {
            Product p = pDao.getProductById(pid);
            int qty = productQuantities.getOrDefault(pid, 1);

            OrderItem oi = new OrderItem();
            oi.setProductId(pid);
            oi.setQuantity(qty);
            oi.setPrice(p.getPrice());
            oi.setProductName(p.getTitle());
            oi.setSellerName(userDao.getUserById(p.getUserId()).getFullName());
            oi.setProduct(p);
            oi.setUser(userDao.getUserById(p.getUserId()));

            bySeller.computeIfAbsent(p.getUserId(), k -> new ArrayList<>()).add(oi);
        }
        return bySeller;
    }

    // Generate next available OrderId
    public String getNextOrderId(String currentMaxOrderId, int index) {
        int orderNumber = Integer.parseInt(currentMaxOrderId.replaceAll("\\D+", "")) + index;
        return String.format("ORD%04d", orderNumber);
    }

    // Create list of Orders from grouped items
    public List<Order> createPendingOrders(Map<String, List<OrderItem>> bySeller, String startOrderId) {
        List<Order> pendingOrders = new ArrayList<>();
        int orderNumber = Integer.parseInt(startOrderId.replaceAll("\\D+", ""));
        int index = 0;

        for (Map.Entry<String, List<OrderItem>> entry : bySeller.entrySet()) {
            String sellerId = entry.getKey();
            List<OrderItem> items = entry.getValue();
            String orderId = String.format("ORD%04d", orderNumber + index);
            int total = items.stream().mapToInt(i -> i.getPrice() * i.getQuantity()).sum();

            Order order = new Order();
            order.setOrderId(orderId);
            order.setUserId(sellerId);
            order.setTotalAmount(total);
            order.setStatus("pending");
            order.setListItems(items);

            pendingOrders.add(order);
            index++;
        }
        return pendingOrders;
    }

    // Parse quantity parameters from request
    public Map<String, Integer> parseProductQuantities(jakarta.servlet.http.HttpServletRequest request, String[] productIds) {
        Map<String, Integer> productQuantities = new HashMap<>();
        for (String pid : productIds) {
            int qty = Integer.parseInt(request.getParameter("qty_" + pid));
            productQuantities.put(pid, qty);
        }
        return productQuantities;
    }
}
