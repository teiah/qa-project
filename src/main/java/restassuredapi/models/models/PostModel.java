package restassuredapi.models.models;

import restassuredapi.models.basemodel.BaseModel;

import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class PostModel extends BaseModel {

    private CategoryModel category;
    private List<CommentModel> comments;
    private String content;
    private String date;
    private boolean isLiked;
    private Set<UserModel> likes;
    private String picture;
    private Integer postId;
    private boolean isPublic;
    private int rank;
    private UserModel user;

    public PostModel() {
    }

    public CategoryModel getCategory() {
        return category;
    }

    public List<CommentModel> getComments() {
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

    public Set<UserModel> getLikes() {
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

    public UserModel getUser() {
        return user;
    }

}
