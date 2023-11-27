package api.models;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class PostResponse {

    private int postId;
    private String content;
    private String picture;
    private String date;
    private UserResponse[] likes;
    private int rank;
    @SerializedName("public") private boolean isPublic;
    private boolean liked;
    private Category category;

}
