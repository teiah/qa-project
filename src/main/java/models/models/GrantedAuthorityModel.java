package models.models;

import models.basemodel.BaseModel;

public class GrantedAuthorityModel extends BaseModel {
    private String authority;

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

}
