package api.models;

import static api.utils.JSONRequests.personalProfileBody;

public class PersonalProfileModel {
    private Integer id;
    private String firstName;
    private String lastName;
    private String sex;
    private LocationModel location = new LocationModel();
    private String birthYear;
    private String personalReview;
    private String memberSince;
    private String picture;
    private boolean picturePrivacy;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public void setPersonalReview(String personalReview) {
        this.personalReview = personalReview;
    }

    public void setMemberSince(String memberSince) {
        this.memberSince = memberSince;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setPicturePrivacy(boolean picturePrivacy) {
        this.picturePrivacy = picturePrivacy;
    }

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

    public boolean getPicturePrivacy() {
        return picturePrivacy;
    }

    @Override
    public String toString() {
        return String.format(personalProfileBody, birthYear, firstName, id, lastName, location.getCity().toString(), personalReview, picture,
                picturePrivacy, sex);
    }
}
