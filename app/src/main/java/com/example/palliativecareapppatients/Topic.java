package com.example.palliativecareapppatients;

public class Topic {

    private String id;
    private String title;
    private String description;
private boolean is_followed;
    public Topic() {}

    public Topic(String id, String title, String description,boolean is_followed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.is_followed=is_followed;
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
    public boolean getIs_followed() {
        return is_followed;
    }

    public void setIs_followed(boolean is_followed) {
        this.is_followed = is_followed;
    }
}
