/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.Entity;

/**
 *
 * @author Thanh Loc
 */
public class ProductImage {
    private int id;
    private int productId;
    private String imageUrl;
    private boolean isPrimary;

    public ProductImage(int id, int productId, String imageUrl, boolean isPrimary) {
        this.id = id;
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.isPrimary = isPrimary;
    }

    public ProductImage() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    
}

