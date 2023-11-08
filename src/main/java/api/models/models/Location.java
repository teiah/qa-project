package api.models.models;

import api.models.basemodel.BaseModel;

public class Location extends BaseModel {
    private City city = new City();
    private Integer id;

    public City getCity() {
        return city;
    }

    public Integer getId() {
        return id;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
