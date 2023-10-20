package models.models;

import models.basemodel.BaseModel;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class RoleModel extends GrantedAuthorityModel {

    private int id;
    private String authority;
    private List<UserModel> users=new ArrayList<>();

}
