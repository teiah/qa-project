package restassuredapi.models.models;

import restassuredapi.models.basemodel.BaseModel;

public class UserByIdModel extends BaseModel {
    private String[] authorities;
    private String birthYear;
    private CityModel city;
    private String email;
    private String expertise;
    private String firstName;
    private String[] gender;
    private int id;
    private String lastNAme;
    private String personalReview;
    private SkillModel[] skills;
    private String username;

    public String[] getAuthorities() {
        return authorities;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public CityModel getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public String getExpertise() {
        return expertise;
    }

    public String getFirstName() {
        return firstName;
    }

    public String[] getGender() {
        return gender;
    }

    public int getId() {
        return id;
    }

    public String getLastNAme() {
        return lastNAme;
    }

    public String getPersonalReview() {
        return personalReview;
    }

    public SkillModel[] getSkills() {
        return skills;
    }

    public String getUsername() {
        return username;
    }
}
