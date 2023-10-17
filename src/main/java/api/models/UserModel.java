package api.models;

import java.util.ArrayList;
import java.util.List;

public class UserModel {

    private List<GrantedAuthorityModel> authorities = new ArrayList<>();
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private String email;
    private boolean enabled = true;
    private ExpertiseProfileModel expertiseProfile = new ExpertiseProfileModel();
    private String password;
    private PersonalProfileModel personalProfile = new PersonalProfileModel();
    private int userId;
    private String username;

    public List<GrantedAuthorityModel> getAuthorities() {
        return authorities;
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

    public ExpertiseProfileModel getExpertiseProfile() {
        return expertiseProfile;
    }

    public String getPassword() {
        return password;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public PersonalProfileModel getPersonalProfile() {
        return personalProfile;
    }

    public int getId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setAuthorities(List<GrantedAuthorityModel> authorities) {
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

    public void setExpertiseProfile(ExpertiseProfileModel expertiseProfile) {
        this.expertiseProfile = expertiseProfile;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPersonalProfile(PersonalProfileModel personalProfile) {
        this.personalProfile = personalProfile;
    }

    public void setId(int id) {
        this.userId = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void addAuthority(RoleModel authority) {
        this.authorities.add(authority);
    }
}
