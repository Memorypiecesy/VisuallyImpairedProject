package com.example.visuallyimpairedproject.bean;

public class AttentionAndFans {
    private Long id;
    private String username;
    private String photo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "AttentionAndFans{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
