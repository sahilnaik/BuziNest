package com.proj.buzinest;

public class User {
    public String Username, email, password, Status, image, Description, ImageURL;
    public User(){

    }

    public User(String Username, String email, String password, String status, String Image, String description, String postImage){
        this.Username = Username;
        this.email = email;
        this.password = password;
        this.Status = status;
        this.image = Image;
        this.Description = description;
        this.ImageURL = postImage;

    }
}
