package com.example.palliativecareapppatients;

import java.util.HashMap;
import java.util.Map;

public class ChatMessage {

    private String messageText;
    private String senderId;
    private long timestamp;

    public ChatMessage() {
        // Default constructor required for Firebase
    }

    public ChatMessage(String messageText, String senderId, long timestamp) {

        this.messageText = messageText;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }





    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // Convert ChatMessage object to a Map for Firebase database
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("messageText", messageText);
        result.put("senderId", senderId);
        result.put("timestamp", timestamp);
        return result;
    }
}
