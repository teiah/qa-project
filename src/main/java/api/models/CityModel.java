package api.models;

public class CityModel {
    private String city;
    private CountryModel country;
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
}
