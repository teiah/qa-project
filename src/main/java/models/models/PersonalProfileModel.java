package models.models;

import models.basemodel.BaseModel;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.response.Response;

import static com.telerikacademy.testframework.utils.Constants.API;
import static com.telerikacademy.testframework.utils.Endpoints.AUTHENTICATE;
import static com.telerikacademy.testframework.utils.Endpoints.UPGRADE_USER_PERSONAL_WITH_ID;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

public class PersonalProfileModel extends BaseModel{

    private final String personalProfileBody = "{\n" +
            "\"birthYear\": \"%s\",\n" +
            "\"firstName\": \"%s\",\n" +
            "\"id\": %d,\n" +
            "\"lastName\": \"%s\",\n" +
            "\"location\": {\n" +
            "\"city\": {\n" +
            "\"city\": \"%s\",\n" +
            "\"country\": {},\n" +
            "\"id\": 1\n" +
            "},\n" +
            "\"id\": 0\n" +
            "},\n" +
            "\"memberSince\": \"\",\n" +
            "\"personalReview\": \"%s\",\n" +
            "\"picture\": \"%s\",\n" +
            "\"picturePrivacy\": %s,\n" +
            "\"sex\": \"%s\"\n" +
            "}";
    private final String personalProfileBodyFirstName = "{\n" +
            "\"firstName\": \"%s\",\n" +
            "\"location\": {\n" +
            "}\n" +
            "}";
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

    public PersonalProfileModel() {
    }

    public PersonalProfileModel(String firstName) {
        this.setFirstName(firstName);
    }

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

    public int getId() {
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

    public void updatePersonalProfile(int userId, String username, String password) {

        boolean picturePrivacy = true;
        String sex = "MALE";
        this.setBirthYear(generateBirthdayDate());
        this.setLastName(generateLastName());
        this.location.getCity().setCity(generateCity());
        this.setPersonalReview(generatePersonalReview());
        this.setPicture(generatePicture());
        this.setPicturePrivacy(picturePrivacy);
        this.setSex(sex);

        String body = String.format(personalProfileBody, birthYear, firstName, id, lastName, this.location.getCity().getCity(),
                personalReview, picture, picturePrivacy, sex);

        Response editProfileResponse = given()
                .auth()
                .form(username, password,
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .queryParam("name",username)
                .body(body)
                .post(String.format(API + UPGRADE_USER_PERSONAL_WITH_ID, userId));

        int statusCode = editProfileResponse.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        LOGGER.info(String.format("Personal profile of user %s with id %d was updated", username, userId));

    }

    public void assertUpdatePersonalProfile(PersonalProfileModel personalProfileBeforeUpdate) {

        assertEquals(personalProfileBeforeUpdate.getFirstName(), this.firstName, "First names do not match.");
        assertEquals(personalProfileBeforeUpdate.getLastName(), this.lastName,"Last names do not match.");
        assertEquals(personalProfileBeforeUpdate.getBirthYear(), this.birthYear,
                "Birth years do not match.");
        assertEquals(personalProfileBeforeUpdate.getLocation().getCity().getCity(), this.location.getCity().getCity(), "Cities do not match.");
        assertEquals(personalProfileBeforeUpdate.getPersonalReview(), this.personalReview, "Personal reviews do not match.");
        assertEquals(personalProfileBeforeUpdate.getPicturePrivacy(), this.picturePrivacy, "Picture privacies do not match.");
    }

    public void setPersonalProfileFirstName(int userId, String username, String password, String firstName) {

        String body = String.format(personalProfileBodyFirstName, firstName);

        Response editProfileResponse = given()
                .auth()
                .form(username, password,
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .body(body)
                .post(String.format(API + UPGRADE_USER_PERSONAL_WITH_ID, userId));

        int statusCode = editProfileResponse.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");
        this.setFirstName(firstName);
        LOGGER.info(String.format("First name of user %s with id %d was set to %s.", username, userId, this.firstName));
    }


//    @Override
//    public String toString() {
//        return String.format(personalProfileBody, birthYear, firstName, id, lastName, location.getCity(), personalReview, picture,
//                picturePrivacy, sex);
//    }
}
