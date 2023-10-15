package api.models;

import java.util.ArrayList;
import java.util.List;

public class ExpertiseProfileModel {
    private Integer id;
    private List<SkillModel> skills = new ArrayList<>();
    private CategoryModel category = new CategoryModel();
    private double availability;

    public Integer getId() {
        return id;
    }

    public List<SkillModel> getSkills() {
        return skills;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public double getAvailability() {
        return availability;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSkills(List<SkillModel> skills) {
        this.skills = skills;
    }

    public void setCategory(CategoryModel category) {
        this.category = category;
    }

    public void setAvailability(double availability) {
        this.availability = availability;
    }

}
