package com.proj.buzinest;

public class PostImages {
    private String ImageURL, Description;

    public PostImages() {

    }

    public PostImages(String imageURL, String description) {
        this.ImageURL = imageURL;
        this.Description = description;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
