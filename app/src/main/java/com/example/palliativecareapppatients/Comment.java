package com.example.palliativecareapppatients;

public class Comment  {




    private String content;
    private String authorName;
    private String userId;



    public Comment(String userId, String authorName, String commentText) {
        this.content=commentText;
        this.authorName=authorName;
        this.userId=userId;

    }
    public Comment() {
        // Default constructor required for Firebase
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }


}
