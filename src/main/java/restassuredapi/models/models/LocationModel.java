package restassuredapi.models.models;

import restassuredapi.models.basemodel.BaseModel;

public class LocationModel extends BaseModel {
    private CityModel city = new CityModel();
    private Integer id;

    public CityModel getCity() {
        return city;
    }

    public Integer getId() {
        return id;
    }

    public void setCity(CityModel city) {
        this.city = city;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
