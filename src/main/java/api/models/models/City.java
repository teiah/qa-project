package api.models.models;

import api.models.basemodel.BaseModel;

public class City extends BaseModel {
    private String city = "";
    private Country country = new Country();
    private Integer id;

    public String getCity() {
        return city;
    }

    public Country getCountry() {
        return country;
    }

    public Integer getId() {
        return id;
    }

    public String toString() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
