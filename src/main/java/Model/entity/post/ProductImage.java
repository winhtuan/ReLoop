package Model.entity.post;

public class ProductImage {
    private int imgId;
    private String productId;
    private String imageUrl;
    private boolean isPrimary;

    public ProductImage() {}

    public ProductImage(int imgId, String productId, String imageUrl, boolean isPrimary) {
        this.imgId = imgId;
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.isPrimary = isPrimary;
    }

    @Override
    public String toString() {
        return "ProductImage{" + "imgId=" + imgId + ", productId=" + productId + ", imageUrl=" + imageUrl + ", isPrimary=" + isPrimary + '}';
    }

    public int getImgId() { return imgId; }
    public void setImgId(int imgId) { this.imgId = imgId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isPrimary() { return isPrimary; }
    public void setPrimary(boolean primary) { isPrimary = primary; }
}
