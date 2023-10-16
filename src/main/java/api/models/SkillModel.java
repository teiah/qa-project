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

    public void setSkillId(Integer skillId) {
        this.skillId = skillId;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public void setCategory(CategoryModel category) {
        this.category = category;
    }
}
