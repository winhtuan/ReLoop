package Model.entity.pay;

import java.util.Date;

public class Payment {
    private String payId;
    private String orderId;
    private int amount;
    private String status;
    private Date paidAt;
    private Date createdAt;
    private Date updatedAt;

    public Payment() {}

    public Payment(String payId, String userId, int amount, String status,
                    Date paidAt, Date createdAt, Date updatedAt) {
        this.payId = payId;
        this.orderId = userId;
        this.amount = amount;
        this.status = status;
        this.paidAt = paidAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getPayId() { return payId; }
    public void setPayId(String payId) { this.payId = payId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getPaidAt() { return paidAt; }
    public void setPaidAt(Date paidAt) { this.paidAt = paidAt; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}