package com.example.palliativecareapppatients;

import org.w3c.dom.Comment;

import java.util.List;

public class Post {
    private String postId;
    private String title;
    private String description;
    private String userId;
    private String imageUrl;
    private String videoUrl;
    private String fileUrl;
    private long timestamp;
    private String topicId;
    private List<Comment> comments;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }


    public Post(String title, String description, String userId, String topicId) {
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.timestamp = System.currentTimeMillis();
        this.topicId = topicId;


    }
    public void setComments(List<Comment>  comments) {
        this.comments = comments;
    }
    public List<Comment> getComments() {
        return comments;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String authorId) {
        this.userId = authorId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topic) {
        this.topicId = topic;
    }
}
