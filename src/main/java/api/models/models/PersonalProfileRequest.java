package api.models.models;

import api.models.BaseModel;
import com.telerikacademy.testframework.models.PersonalProfile;
import com.telerikacademy.testframework.models.User;
import com.telerikacademy.testframework.utils.Gender;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PersonalProfileRequest extends BaseModel {

    private String firstName;
    private String lastName;
    private Gender gender;
    private ApiLocation location;
    private String birthYear;
    private String personalReview;
    private String memberSince;
    private String picture;
    private boolean picturePrivacy;

    public PersonalProfileRequest(User user) {
        PersonalProfile profile = user.getProfile();
        birthYear = profile.getBirthday();
        firstName = profile.getFirstName();
        lastName = profile.getLastName();
        location = new ApiLocation(profile.getLocation());
        picturePrivacy = profile.getPicturePrivacy();
        gender = profile.getGender();
        picture = profile.getPicture();
        personalReview = profile.getPersonalReview();
    }

    public boolean getPicturePrivacy() {
        return picturePrivacy;
    }
}