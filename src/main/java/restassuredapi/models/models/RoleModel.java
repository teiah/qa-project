package restassuredapi.models.models;

import java.util.ArrayList;
import java.util.List;

public class RoleModel extends GrantedAuthorityModel {

    private int id;
    private String authority;
    private List<UserModel> users=new ArrayList<>();

}
