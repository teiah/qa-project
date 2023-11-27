package api.models;

import java.util.HashSet;
import java.util.Set;

public class Comment extends BaseModel {
    private Integer commentId;
    private Post post;
    private UserRequest user;
    private String content;
    private Set<UserRequest> likes = new HashSet<>();
    private String date;
    private boolean liked = false;

    public Integer getCommentId() {
        return commentId;
    }

    public Post getPost() {
        return post;
    }

    public UserRequest getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public Set<UserRequest> getLikes() {
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

    public void setPost(Post post) {
        this.post = post;
    }

    public void setUser(UserRequest user) {
        this.user = user;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLikes(Set<UserRequest> likes) {
        this.likes = likes;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

}
