package api.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllUsersResponse {

    private int userId;
    private String username;
    private ExpertiseProfileRequest expertiseProfile;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;

}
