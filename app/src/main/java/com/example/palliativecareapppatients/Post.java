package com.example.palliativecareapppatients;

public class Post {
    private String postId;
    private String postText;
    private String postImage;
    private String postVideo;
    private String postGif;
    private String postFile;
    private String postDoctorName;
    private String postTopicId;
    private String postTime;
    private int type;
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_VIDEO = 1;
    public static final int TYPE_INFOGRAPHIC = 2;
    public static final int TYPE_PDF = 3;
    public static final int TYPE_DEFAULT = 4;
    public Post(String postId ,String postTopicId, String postText, String postImage, String postVideo, String postGif, String postFile, String postDoctorName, String postTime) {
        this.postId = postId;
        this.postText = postText;
        this.postImage = postImage;
        this.postVideo = postVideo;
        this.postGif = postGif;
        this.postFile = postFile;
        this.postDoctorName = postDoctorName;
        this.postTime = postTime;
        this.postTopicId=postTopicId;
    }

    // Getters and Setters

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostVideo() {
        return postVideo;
    }

    public void setPostVideo(String postVideo) {
        this.postVideo = postVideo;
    }

    public String getPostGif() {
        return postGif;
    }

    public void setPostGif(String postGif) {
        this.postGif = postGif;
    }

    public String getPostFile() {
        return postFile;
    }

    public void setPostFile(String postFile) {
        this.postFile = postFile;
    }

    public String getPostDoctorName() {
        return postDoctorName;
    }

    public void setPostDoctorName(String postDoctorName) {
        this.postDoctorName = postDoctorName;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }
    public int getType() {
        return type;
    }

    // Setter for post type
    public void setType(int type) {
        this.type = type;
    }
}
