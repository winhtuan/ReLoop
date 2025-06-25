package Model.entity.conversation;

public class Conversation {
    private String conversationId;
    private String senderId;
    private String receiverId;
    private String productId;

    public Conversation() {}

    public Conversation(String conversationId, String senderId, String receiverId, String productId) {
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.productId = productId;
    }

    public String getConversationId() { return conversationId; }
    public void setConversationId(String conversationId) { this.conversationId = conversationId; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    @Override
    public String toString() {
        return "Conversation{" + "conversationId=" + conversationId + ", senderId=" + senderId + ", receiverId=" + receiverId + ", productId=" + productId + '}';
    }
    
    
}
