package Model.entity.conversation;

public class BlockUserConversation {
    private String blockerUserId;
    private String blockedUserId;

    public BlockUserConversation() {}

    public BlockUserConversation(String blockerUserId, String blockedUserId) {
        this.blockerUserId = blockerUserId;
        this.blockedUserId = blockedUserId;
    }

    public String getBlockerUserId() { return blockerUserId; }
    public void setBlockerUserId(String blockerUserId) { this.blockerUserId = blockerUserId; }

    public String getBlockedUserId() { return blockedUserId; }
    public void setBlockedUserId(String blockedUserId) { this.blockedUserId = blockedUserId; }
}
