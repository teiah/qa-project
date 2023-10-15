package api.models;

import static api.utils.JSONRequests.personalProfileBody;

public class PersonalProfileModel {
    private Integer id;
    private String firstName;
    private String lastName;
    private String sex;
    private LocationModel location;
    private String birthYear;
    private String personalReview;
    private String memberSince;
    private String picture;
    private String picturePrivacy;

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSex() {
        return sex;
    }

    public LocationModel getLocation() {
        return location;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public String getPersonalReview() {
        return personalReview;
    }

    public String getMemberSince() {
        return memberSince;
    }

    public String getPicture() {
        return picture;
    }

    public String getPicturePrivacy() {
        return picturePrivacy;
    }

    @Override
    public String toString() {
        return String.format(personalProfileBody, birthYear, firstName, id, lastName, location.getCity().toString(), personalReview, picture,
                picturePrivacy, sex);
    }
}
