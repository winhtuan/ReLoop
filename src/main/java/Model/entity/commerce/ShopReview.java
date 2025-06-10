package Model.entity.commerce;

import java.util.Date;

public class ShopReview {
    private String reviewId;
    private String reviewerId;
    private String sellerId;
    private int rating;
    private String comment;
    private Date reviewDate;

    public ShopReview() {}

    public ShopReview(String reviewId, String reviewerId, String sellerId,
                      int rating, String comment, Date reviewDate) {
        this.reviewId = reviewId;
        this.reviewerId = reviewerId;
        this.sellerId = sellerId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }

    public String getReviewId() { return reviewId; }
    public void setReviewId(String reviewId) { this.reviewId = reviewId; }

    public String getReviewerId() { return reviewerId; }
    public void setReviewerId(String reviewerId) { this.reviewerId = reviewerId; }

    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Date getReviewDate() { return reviewDate; }
    public void setReviewDate(Date reviewDate) { this.reviewDate = reviewDate; }
}
