package com.example.visuallyimpairedproject.bean;

public class CommentsVo {

    private Long articleId;
    private String context;
    private Long parentCommentsId;
    private Long toUserId;

    public CommentsVo(Long articleId, String context, Long parentCommentsId, Long toUserId) {
        this.articleId = articleId;
        this.context = context;
        this.parentCommentsId = parentCommentsId;
        this.toUserId = toUserId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Long getParentCommentsId() {
        return parentCommentsId;
    }

    public void setParentCommentsId(Long parentCommentsId) {
        this.parentCommentsId = parentCommentsId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }
}
