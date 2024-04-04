package com.example.visuallyimpairedproject.bean;

import java.util.List;

public class Comments {
    private String id;
    private User user;
    private String content;
    private List<Comments> childrens;
    private String createDate;
    private Integer level;
    private Integer likeCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Comments> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<Comments> childrens) {
        this.childrens = childrens;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }
}
