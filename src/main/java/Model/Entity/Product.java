/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.Entity;

/**
 *
 * @author Thanh Loc
 */
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Product {
    private int id;
    private int userId;
    private int categoryId;
    private String title;
    private String description;
    private BigDecimal price;
    private String location;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private List<ProductImage> images;

    public Product(int id, int userId, int categoryId, String title, String description, BigDecimal price, String location, String status, Date createdAt, Date updatedAt, List<ProductImage> images) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.location = location;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.images = images;
        
    }

    

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", userId=" + userId + ", categoryId=" + categoryId + ", title=" + title + ", description=" + description + ", price=" + price + ", location=" + location + ", status=" + status + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", images=" + images + '}';
    }
    
    
}
