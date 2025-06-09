/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.Entity;

import java.sql.Timestamp;

public class Message {
    private int messageId;
    private int conversationId;
    private int senderId;
    private String content;
    private Timestamp timestamp;
    private boolean is_recall;
    private boolean is_Read;
    private String type;

    public Message() {}

    public Message(int messageId, int conversationId, int senderId, String content, Timestamp timestamp, boolean isre, boolean isread, String type) {
        this.messageId = messageId;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
        this.is_recall=isre;
        this.is_Read=isread;
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public boolean isIs_Read() {
        return is_Read;
    }

    public void setIs_Read(boolean is_Read) {
        this.is_Read = is_Read;
    }

    
    
    // Getters and Setters
    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isIs_recall() {
        return is_recall;
    }

    public void setIs_recall(boolean is_recall) {
        this.is_recall = is_recall;
    }
    
}
