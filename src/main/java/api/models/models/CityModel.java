package api.models.models;

import api.models.basemodel.BaseModel;

public class CityModel extends BaseModel {
    private String city = "";
    private CountryModel country = new CountryModel();
    private Integer id;

    public String getCity() {
        return city;
    }

    public CountryModel getCountry() {
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

    public void setCountry(CountryModel country) {
        this.country = country;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
