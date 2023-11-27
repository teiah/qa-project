package api.models;

import api.models.BaseModel;
import com.telerikacademy.testframework.utils.Expertise;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Category extends BaseModel {
    private Integer id;
    private String name;

    public Category(Expertise category) {
        setId(category.getId());
        setName(category.getStringValue());
    }
}
