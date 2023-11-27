package api.models;

import com.google.gson.annotations.SerializedName;
import com.telerikacademy.testframework.models.Post;
import com.telerikacademy.testframework.utils.Visibility;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest extends BaseModel {

    private String content;
    private String picture;
    @SerializedName("public")
    private boolean isPublic;

    public PostRequest(Post post) {
        setContent(post.getContent());
        setPicture("");
        setPublic(post.getVisibility().equals(Visibility.PUBLIC));
    }
}