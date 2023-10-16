package api.models;

import org.checkerframework.checker.units.qual.C;

public class LocationModel {
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
