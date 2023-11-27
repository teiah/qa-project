package api.models;

import com.telerikacademy.testframework.utils.Location;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class ApiLocation extends BaseModel {

    public ApiLocation(Location location) {
        city = new City(location);
        id = 1;
    }

    private City city;
    private int id;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiLocation l = (ApiLocation) o;
        return Objects.deepEquals(this.id, l.id) &&
                Objects.deepEquals(this.city, l.city);
    }
}
