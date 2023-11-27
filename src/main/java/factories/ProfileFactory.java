package factories;


import com.telerikacademy.testframework.utils.Helpers;
import com.telerikacademy.testframework.models.PersonalProfile;



public class ProfileFactory {

    public static PersonalProfile createProfile() {
        PersonalProfile profile = new PersonalProfile(
                Helpers.generateFirstName(), Helpers.generateLastName(),
                Helpers.generateBirthdayDate(), Helpers.generateCity() );
        profile.setGender(Helpers.generateGender());
        profile.setPersonalReview(Helpers.generatePersonalReview());
        return profile;
    }

    public static PersonalProfile createProfileWithSkills() {
        PersonalProfile profile = new PersonalProfile(
                Helpers.generateFirstName(), Helpers.generateLastName(),
                Helpers.generateBirthdayDate(), Helpers.generateCity());
        profile.setGender(Helpers.generateGender());

        profile.setSkills(ExpertiseFactory.createSkills());
        return profile;
    }
}
