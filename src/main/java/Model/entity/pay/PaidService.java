package Model.entity.pay;

import java.util.Date;

public class PaidService {

    private String paidId;
    private String serviceName;
    private double price;
    private Date startDate;
    private Date endDate;
    private int usageTime;

    public PaidService() {
    }

    public PaidService(String paidId, String serviceName, double price, Date startDate, Date endDate, int usageTime) {
        this.paidId = paidId;
        this.serviceName = serviceName;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.usageTime = usageTime;
    }

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(int usageTime) {
        this.usageTime = usageTime;
    }
}
