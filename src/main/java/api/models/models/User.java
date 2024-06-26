package api.models.models;

import com.telerikacademy.testframework.utils.Authority;

import java.util.List;

import static org.testng.Assert.assertNotEquals;
import static io.restassured.RestAssured.given;

public class User {

    private List<Authority> authorities;
    private String email;
    private boolean enabled = true;
    private ExpertiseProfile expertiseProfile = new ExpertiseProfile();
    private String password;
    private PersonalProfile personalProfile = new PersonalProfile();
    private int userId;
    private String username;

    private int categoryId;

    public User(int categoryId, String username, List<Authority> authorities, String email, String password, String confirmPassword) {
        this.categoryId = categoryId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        confirmPassword = password;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }


    public String getEmail() {
        return email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ExpertiseProfile getExpertiseProfile() {
        return expertiseProfile;
    }

    public String getPassword() {
        return password;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public PersonalProfile getPersonalProfile() {
        return personalProfile;
    }

    public int getId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setExpertiseProfile(ExpertiseProfile expertiseProfile) {
        this.expertiseProfile = expertiseProfile;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(int id) {
        this.userId = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPersonalProfile(PersonalProfile personalProfileModel) {
        this.personalProfile = personalProfileModel;
    }

}
