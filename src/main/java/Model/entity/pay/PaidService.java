package Model.entity.pay;

public class PaidService {

    private String paidId;
    private String serviceName;
    private int price;
    private String description;
    private int usageTime;

    // Constructor
    public PaidService(String paidId, String serviceName, int price, String description, int usageTime) {
        this.paidId = paidId;
        this.serviceName = serviceName;
        this.price = price;
        this.description = description;
        this.usageTime = usageTime;
    }

    // Getters and Setters
    public String getPaidId() {
        return paidId;
    }

    public void setPaidId(String paidId) {
        this.paidId = paidId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(int usageTime) {
        this.usageTime = usageTime;
    }

}
