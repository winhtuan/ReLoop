package Model.entity.post;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Product {
    private String productId;
    private String userId;
    private Integer categoryId;
    private String title;
    private String description;
    private BigDecimal price;
    private String location;
    private String status;
    private boolean isPriority;
    private Date createdAt;
    private Date updatedAt;
    private List<ProductImage> images;

    public Product() {}

    public Product(String productId, String userId, Integer categoryId, String title, String description, BigDecimal price, String location, String status, boolean isPriority, Date createdAt, Date updatedAt, List<ProductImage> images) {
        this.productId = productId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.location = location;
        this.status = status;
        this.isPriority = isPriority;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.images = images;
    }

    public Product(String productId, String userId, Integer categoryId, String title, String description,
                    BigDecimal price, String location, String status, boolean isPriority, Date createdAt, Date updatedAt) {
        this.productId = productId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.location = location;
        this.status = status;
        this.isPriority = isPriority;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public boolean isIsPriority() {
        return isPriority;
    }

    public void setIsPriority(boolean isPriority) {
        this.isPriority = isPriority;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isPriority() { return isPriority; }
    public void setPriority(boolean priority) { isPriority = priority; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
