package api.models;

public class SkillModel {
    private Integer skillId;
    private String skill;
    private CategoryModel category;

    public Integer getSkillId() {
        return skillId;
    }

    public String getSkill() {
        return skill;
    }

    public CategoryModel getCategory() {
        return category;
    }
}
