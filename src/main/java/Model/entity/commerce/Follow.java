
package Model.entity.commerce;

import java.sql.Timestamp;

public class Follow {
    private String followerId;
    private String followingId;
    private Timestamp followedAt;

    public Follow() {
    }

    public Follow(String followerId, String followingId, Timestamp followedAt) {
        this.followerId = followerId;
        this.followingId = followingId;
        this.followedAt = followedAt;
    }

    public String getFollowerId() {
        return followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    public String getFollowingId() {
        return followingId;
    }

    public void setFollowingId(String followingId) {
        this.followingId = followingId;
    }

    public Timestamp getFollowedAt() {
        return followedAt;
    }

    public void setFollowedAt(Timestamp followedAt) {
        this.followedAt = followedAt;
    }
}
