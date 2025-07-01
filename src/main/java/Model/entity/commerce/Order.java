package Model.entity.commerce;

import java.util.Date;
import java.util.List;

public class Order {
    private String orderId;
    private String userId;
    private int totalAmount;
    private String status;
    private String shippingAddress;
    private Integer shippingMethod;
    private String voucherId;
    private int discountAmount;
    private Date createdAt;
    private Date updatedAt;
    private int shipfee;
    private List<OrderItem> listItems;

    public List<OrderItem> getListItems() {
        return listItems;
    }

    public void setListItems(List<OrderItem> listItems) {
        this.listItems = listItems;
    }

    public Order() {}

    public Order(String orderId, String userId, int totalAmount, String status,
                  String shippingAddress, Integer shippingMethod, String voucherId,
                  int discountAmount, Date createdAt, Date updatedAt) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.shippingMethod = shippingMethod;
        this.voucherId = voucherId;
        this.discountAmount = discountAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Order(String orderId, String userId, int totalAmount, String status, String shippingAddress, Integer shippingMethod, String voucherId, int discountAmount, Date createdAt, Date updatedAt, int shipfee, List<OrderItem> listItems) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.shippingMethod = shippingMethod;
        this.voucherId = voucherId;
        this.discountAmount = discountAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.shipfee = shipfee;
        this.listItems = listItems;
    }

    public int getShipfee() {
        return shipfee;
    }

    public void setShipfee(int shipfee) {
        this.shipfee = shipfee;
    }

    
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public int getTotalAmount() { return totalAmount; }
    public void setTotalAmount(int totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public Integer getShippingMethod() { return shippingMethod; }
    public void setShippingMethod(Integer shippingMethod) { this.shippingMethod = shippingMethod; }

    public String getVoucherId() { return voucherId; }
    public void setVoucherId(String voucherId) { this.voucherId = voucherId; }

    public int getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(int discountAmount) { this.discountAmount = discountAmount; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}