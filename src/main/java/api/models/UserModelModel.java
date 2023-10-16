package api.models;

public class UserModelModel {

    String[] authorities;
    String birthYear;
    CityModel city;
    String email;
    String expertise;
    String firstName;
    String[] gender;
    int id;
    String lastNAme;
    String personalReview;
    String[] skills;
    String username;

    public String[] getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String[] authorities) {
        this.authorities = authorities;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public CityModel getCity() {
        return city;
    }

    public void setCity(CityModel city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String[] getGender() {
        return gender;
    }

    public void setGender(String[] gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastNAme() {
        return lastNAme;
    }

    public void setLastNAme(String lastNAme) {
        this.lastNAme = lastNAme;
    }

    public String getPersonalReview() {
        return personalReview;
    }

    public void setPersonalReview(String personalReview) {
        this.personalReview = personalReview;
    }

    public String[] getSkills() {
        return skills;
    }

    public void setSkills(String[] skills) {
        this.skills = skills;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
