package restassuredapi.models.models;

import restassuredapi.models.basemodel.BaseModel;

public class PersonalProfileModel extends BaseModel{

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

    @Override
    public String toString() {
        String personalProfileBody = "{\n" +
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

        String birthYear = this.getBirthYear();
        String firstName = this.getFirstName();
        String lastName = this.getLastName();
        String city = "";
        if (this.getLocation() != null) {
            city = this.getLocation().getCity().getCity();
        }
        String personalReview = this.getPersonalReview();
        String picture = this.getPicture();
        boolean picturePrivacy = this.getPicturePrivacy();
        String sex = this.getSex();

        return String.format(personalProfileBody, birthYear, firstName, this.getId(), lastName, city, personalReview,
                picture, picturePrivacy, sex);
    }

}
