package api.models.models;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class UserResponse {

    private int id;
    private String username;
    private List<String> authorities;
    private String email;
    private String firstName;
    // this field has a typo
    @SerializedName("lastNAme")
    private String lastName;
    private String gender;
    private City city;
    private String birthYear;
    private String personalReview;
    private String expertise;
    private String[] skills;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof UserResponse)) return false;
        UserResponse user = (UserResponse) o;
        return username.equals(user.getUsername()) && id == user.getId();
    }
}
