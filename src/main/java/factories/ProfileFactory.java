package factories;

import api.models.models.ApiLocation;
import api.models.models.PersonalProfile;
import com.telerikacademy.testframework.utils.Helpers;
import com.telerikacademy.testframework.utils.Location;

public class ProfileFactory {

    public static PersonalProfile createProfile() {

        String birthYear = Helpers.generateBirthdayDate();
        String firstName = Helpers.generateFirstName();
        String lastName = Helpers.generateLastName();
        Location city = Helpers.generateCity();

        String personalReview = Helpers.generatePersonalReview();
        String picture = Helpers.generatePicture();
        boolean picturePrivacy = Helpers.generatePrivacy();
        String sex = Helpers.generateSex();

        ApiLocation location = new ApiLocation(city);
        return new PersonalProfile(
                firstName, lastName, sex, location, birthYear, personalReview, picture, picturePrivacy);

    }
}
