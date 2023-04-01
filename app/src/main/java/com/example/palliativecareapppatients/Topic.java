package com.example.palliativecareapppatients;

public class Topic {

    private String id;
    private String title;
    private String description;
    boolean isFollowed ;

    public Topic() {
        // Required empty public constructor
    }

    public Topic(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isFollowed = isFollowed;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }
}

