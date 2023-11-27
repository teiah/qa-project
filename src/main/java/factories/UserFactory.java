package factories;

import com.telerikacademy.testframework.models.PersonalProfile;
import com.telerikacademy.testframework.models.User;
import com.telerikacademy.testframework.utils.Helpers;


import static com.telerikacademy.testframework.utils.Authority.ROLE_USER;
import static com.telerikacademy.testframework.utils.Authority.ROLE_ADMIN;

public class UserFactory {

    public static User createUser() {
        return new User(Helpers.generateUsername(ROLE_USER.toString()), Helpers.generateEmail(),
                Helpers.generatePassword(), Helpers.generateExpertise());
    }

    public static User createAdmin() {

        return new User(Helpers.generateUsername(ROLE_ADMIN.toString()), Helpers.generateEmail(),
                Helpers.generatePassword(), Helpers.generateExpertise());
    }

    public static User createUserWithProfile(PersonalProfile profile) {
        return new User(Helpers.generateUsername(ROLE_USER.toString()), Helpers.generateEmail(),
                Helpers.generatePassword(), Helpers.generateExpertise(), profile);
    }
}
