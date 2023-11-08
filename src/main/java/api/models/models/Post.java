package api.models.models;

import api.models.basemodel.BaseModel;

import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class Post extends BaseModel {

    private Category category;
    private List<Comment> comments;
    private String content;
    private String date;
    private boolean isLiked;
    private Set<User> likes;
    private String picture;
    private Integer postId;
    private boolean isPublic;
    private int rank;
    private User user;

    public Post() {
    }

    public Category getCategory() {
        return category;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public Set<User> getLikes() {
        return likes;
    }

    public String getPicture() {
        return picture;
    }

    public Integer getPostId() {
        return postId;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public int getRank() {
        return rank;
    }

    public User getUser() {
        return user;
    }

}
