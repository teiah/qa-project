package api.models.models;

import api.models.basemodel.BaseModel;
import com.telerikacademy.testframework.utils.Location;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class City extends BaseModel {


    public City(Location location) {
        id = location.getId();
        city = location.getStringValue();
    }

    private int id;
    private String city;
    private Country country;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City c = (City) o;
        return Objects.deepEquals(this.id, c.id) &&
                Objects.deepEquals(this.city, c.city) &&
                Objects.deepEquals(this.country, c.country);

    }
}

