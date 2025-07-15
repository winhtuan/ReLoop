package Model.entity.pay;

import java.util.Date;

public class Voucher {
    private String voucherId;
    private String code;
    private String description;
    private int discountValue;
    private int minOrderAmount;
    private Date startDate;
    private Date endDate;
    private int usageLimit;
    private int usedCount;
    private boolean isActive;

    public Voucher() {}

    public Voucher(String voucherId, String code, String description, String discountType, int discountValue,
                    int maxDiscount, int minOrderAmount, Date startDate, Date endDate,
                    int usageLimit, int usedCount, boolean isActive) {
        this.voucherId = voucherId;
        this.code = code;
        this.description = description;
        this.discountValue = discountValue;
        this.minOrderAmount = minOrderAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.usageLimit = usageLimit;
        this.usedCount = usedCount;
        this.isActive = isActive;
    }

    // Getters and Setters
    public String getVoucherId() { return voucherId; }
    public void setVoucherId(String voucherId) { this.voucherId = voucherId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getDiscountValue() { return discountValue; }
    public void setDiscountValue(int discountValue) { this.discountValue = discountValue; }

    public int getMinOrderAmount() { return minOrderAmount; }
    public void setMinOrderAmount(int minOrderAmount) { this.minOrderAmount = minOrderAmount; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public int getUsageLimit() { return usageLimit; }
    public void setUsageLimit(int usageLimit) { this.usageLimit = usageLimit; }

    public int getUsedCount() { return usedCount; }
    public void setUsedCount(int usedCount) { this.usedCount = usedCount; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}