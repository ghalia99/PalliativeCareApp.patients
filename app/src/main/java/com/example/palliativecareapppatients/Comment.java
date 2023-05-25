package com.example.palliativecareapppatients;

public class Comment  {

     public Comment(String content,String authorName){
         this.content=content;
         this.authorName=authorName;
     }


    public void setContent(String content) {
        this.content = content;
    }

    public void setUserName(String authorName) {
        this.authorName = authorName;
    }

    private String content;
    private String authorName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;

    public String getContent() {
        return content;
    }

    public String getUserName() {
        return authorName;
    }


}
