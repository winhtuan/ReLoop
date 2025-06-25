package Model.entity.conversation;

import java.sql.Timestamp;
import java.util.Date;

public class Message {
    private String messageId;
    private String conversationId;
    private String senderId;
    private String content;
    private boolean isRead;
    private String type;
    private Timestamp sentAt;
    private boolean is_recall;

    public Message() {}

    public Message(String messageId, String conversationId, String senderId, String content,
                    boolean isRead, String type, Timestamp sentAt, boolean isRecall) {
        this.messageId = messageId;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.content = content;
        this.isRead = isRead;
        this.type = type;
        this.sentAt = sentAt;
        this.is_recall = isRecall;
    }

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getConversationId() { return conversationId; }
    public void setConversationId(String conversationId) { this.conversationId = conversationId; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Date getSentAt() { return sentAt; }
    public void setSentAt(Timestamp sentAt) { this.sentAt = sentAt; }

    public boolean isRecall() { return is_recall; }
    public void setRecall(boolean recall) { is_recall = recall; }
}
