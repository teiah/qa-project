package api.models.models;

import api.models.basemodel.BaseModel;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertNotEquals;
import static io.restassured.RestAssured.given;

public class User extends BaseModel {

    private List<Authority> authorities = new ArrayList<>();
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private String email;
    private boolean enabled = true;
    private ExpertiseProfile expertiseProfile = new ExpertiseProfile();
    private String password;
    private PersonalProfile personalProfile = new PersonalProfile();
    private int userId;
    private String username;

    public User() {
    }

    public List<Authority> getAuthorities() {
        return this.authorities;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
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

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
