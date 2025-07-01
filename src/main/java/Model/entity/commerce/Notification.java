package Model.entity.commerce;

import java.sql.Timestamp;

public class Notification {

    private String notiId;
    private String userId;
    private String title;
    private String content;
    private String link;
    private String type;
    private Timestamp createdAt;
    private boolean isRead;

    public Notification() {}

    public Notification(String notiId, String userId, String title, String content, String link, String type, Timestamp createdAt, boolean isRead) {
        this.notiId = notiId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.link = link;
        this.type = type;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    public String getNotiId() { return notiId; }
    public void setNotiId(String notiId) { this.notiId = notiId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean isRead) { this.isRead = isRead; }
}
