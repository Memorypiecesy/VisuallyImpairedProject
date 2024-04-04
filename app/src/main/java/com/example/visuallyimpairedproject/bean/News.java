package com.example.visuallyimpairedproject.bean;

public class News {
    private Long id;
    private Long authorId;
    private String authorName;
    private String authorPhoto;
    private String title;
    private String category;
    private String context;
    private Integer viewCount;
    private String finalTime;
    private String contextImage;
    private Integer deleteId;
    private Integer commentsCount;
    private Integer collectCount;
    private Integer likeCount;
    private String photoContext;
    private Integer isSuccessUp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorPhoto() {
        return authorPhoto;
    }

    public void setAuthorPhoto(String authorPhoto) {
        this.authorPhoto = authorPhoto;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public String getFinalTime() {
        return finalTime;
    }

    public void setFinalTime(String finalTime) {
        this.finalTime = finalTime;
    }

    public String getContextImage() {
        return contextImage;
    }

    public void setContextImage(String contextImage) {
        this.contextImage = contextImage;
    }

    public Integer getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(Integer deleteId) {
        this.deleteId = deleteId;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Integer getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public String getPhotoContext() {
        return photoContext;
    }

    public void setPhotoContext(String photoContext) {
        this.photoContext = photoContext;
    }

    public Integer getIsSuccessUp() {
        return isSuccessUp;
    }

    public void setIsSuccessUp(Integer isSuccessUp) {
        this.isSuccessUp = isSuccessUp;
    }

    public String getFalseCause() {
        return falseCause;
    }

    public void setFalseCause(String falseCause) {
        this.falseCause = falseCause;
    }

    private String falseCause;
}
