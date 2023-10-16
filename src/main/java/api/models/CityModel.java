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
