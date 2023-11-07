package api.models.models;

import api.models.basemodel.BaseModel;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class UserBySearchModel extends BaseModel {

    private int userId;
    private String username;
    private ExpertiseProfileModel expertiseProfile = new ExpertiseProfileModel();
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean accountNonLocked;

    public UserBySearchModel(int userId, String firstName) {

    }



    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ExpertiseProfileModel getExpertiseProfile() {
        return expertiseProfile;
    }

    public void setExpertiseProfile(ExpertiseProfileModel expertiseProfile) {
        this.expertiseProfile = expertiseProfile;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }
}
