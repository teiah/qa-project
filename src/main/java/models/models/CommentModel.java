package models.models;

import models.basemodel.BaseModel;

import java.util.HashSet;
import java.util.Set;

public class CommentModel extends BaseModel {
    private Integer commentId;
    private PostModel post;
    private UserModel user;
    private String content;
    private Set<UserModel> likes = new HashSet<>();
    private String date;
    private boolean liked = false;

    public Integer getCommentId() {
        return commentId;
    }

    public PostModel getPost() {
        return post;
    }

    public UserModel getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public Set<UserModel> getLikes() {
        return likes;
    }

    public String getDate() {
        return date;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public void setPost(PostModel post) {
        this.post = post;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLikes(Set<UserModel> likes) {
        this.likes = likes;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

}
