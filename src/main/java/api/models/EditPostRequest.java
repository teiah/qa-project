package api.models;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditPostRequest {

    public EditPostRequest(String newContent) {
        setContent(newContent);
        setPicture("");
        setPublic(true);
    }

    private String content;
    private String picture;
    @SerializedName("public") private boolean isPublic;

}
