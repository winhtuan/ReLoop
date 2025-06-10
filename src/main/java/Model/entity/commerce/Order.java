package Model.entity.commerce;

import java.math.BigDecimal;
import java.util.Date;

public class Order {
    private String orderId;
    private String userId;
    private BigDecimal totalAmount;
    private String status;
    private String shippingAddress;
    private Integer shippingMethod;
    private String voucherId;
    private BigDecimal discountAmount;
    private Date createdAt;
    private Date updatedAt;

    public Order() {}

    public Order(String orderId, String userId, BigDecimal totalAmount, String status,
                  String shippingAddress, Integer shippingMethod, String voucherId,
                  BigDecimal discountAmount, Date createdAt, Date updatedAt) {
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

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public Integer getShippingMethod() { return shippingMethod; }
    public void setShippingMethod(Integer shippingMethod) { this.shippingMethod = shippingMethod; }

    public String getVoucherId() { return voucherId; }
    public void setVoucherId(String voucherId) { this.voucherId = voucherId; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
