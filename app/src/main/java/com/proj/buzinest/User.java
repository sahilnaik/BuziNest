package com.proj.buzinest;

public class User {
    public String Username, email, password, Status, image;
    public User(){

    }

    public User(String Username, String email, String password, String status, String Image){
        this.Username = Username;
        this.email = email;
        this.password = password;
        this.Status = status;
        this.image = Image;

    }
}
