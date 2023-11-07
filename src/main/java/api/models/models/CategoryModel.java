package api.models.models;

import api.models.basemodel.BaseModel;

public class CategoryModel extends BaseModel {
    private Integer id;
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
