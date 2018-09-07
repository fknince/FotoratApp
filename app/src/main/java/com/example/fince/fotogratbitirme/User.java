package com.example.fince.fotogratbitirme;

/**
 * Created by fince on 25.01.2018.
 */

public class User {


    String email;
    String createdAt;
    public User(){};
    public User(String email,String createdAt)
    {
        this.createdAt=createdAt;
        this.email=email;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
