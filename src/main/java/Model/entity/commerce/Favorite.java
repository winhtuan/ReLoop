package Model.entity.commerce;

import java.util.Date;

public class Favorite {
    private String favId;
    private String userId;
    private String productId;
    private Date favoritedAt;

    public Favorite() {}

    public Favorite(String favId, String userId, String productId, Date favoritedAt) {
        this.favId = favId;
        this.userId = userId;
        this.productId = productId;
        this.favoritedAt = favoritedAt;
    }

    public String getFavId() { return favId; }
    public void setFavId(String favId) { this.favId = favId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public Date getFavoritedAt() { return favoritedAt; }
    public void setFavoritedAt(Date favoritedAt) { this.favoritedAt = favoritedAt; }
}
