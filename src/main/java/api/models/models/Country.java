package api.models.models;

import api.models.basemodel.BaseModel;

public class Country extends BaseModel {
    private Integer id;
    private String country;

    public Integer getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }
}
