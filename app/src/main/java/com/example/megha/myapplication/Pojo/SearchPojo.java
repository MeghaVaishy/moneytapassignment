package com.example.megha.myapplication.Pojo;

public class SearchPojo {
    String imageURL;

    public SearchPojo(String imageURL, String title, String description) {
        this.imageURL = imageURL;
        this.title = title;
        this.description = description;
    }

    String title;
    String description;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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
}
