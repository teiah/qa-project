package api.models.models;

import api.models.BaseModel;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PersonalProfile extends BaseModel {

    private String firstName;
    private String lastName;
    private String sex;
    private ApiLocation location;
    private String birthYear;
    private String personalReview;
    private String memberSince;
    private String picture;
    private boolean picturePrivacy;

    public PersonalProfile(String firstName, String lastName, String sex, ApiLocation location,
                           String birthYear, String personalReview, String picture, boolean picturePrivacy) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.location = location;
        this.birthYear = birthYear;
        this.personalReview = personalReview;
        this.picture = picture;
        this.picturePrivacy = picturePrivacy;
    }

    public boolean getPicturePrivacy() {
        return picturePrivacy;
    }
}