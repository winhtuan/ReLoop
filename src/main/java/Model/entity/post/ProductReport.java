package Model.entity.post;

import java.util.Date;

public class ProductReport {

    private String reportId;
    private String productId;
    private String userId;
    private String reason;
    private String description;
    private Date reportedAt;
    private String status;

    public ProductReport() {
    }

    public ProductReport(String reportId, String productId, String userId, String reason,
            String description, Date reportedAt, String status) {
        this.reportId = reportId;
        this.productId = productId;
        this.userId = userId;
        this.reason = reason;
        this.description = description;
        this.reportedAt = reportedAt;
        this.status = status;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

   

    public String getReason() {
        return reason;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getReportedAt() {
        return reportedAt;
    }

    public void setReportedAt(Date reportedAt) {
        this.reportedAt = reportedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ProductReport{" + "reportId=" + reportId + ", productId=" + productId + ", userId=" + userId + ", reason=" + reason + ", description=" + description + ", reportedAt=" + reportedAt + ", status=" + status + '}';
    }
    
}
