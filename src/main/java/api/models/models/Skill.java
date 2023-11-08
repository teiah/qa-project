package api.models.models;

import api.models.basemodel.BaseModel;

public class Skill extends BaseModel {
    private Integer skillId;
    private String skill;
    private Category category;

    public Integer getSkillId() {
        return skillId;
    }

    public String getSkill() {
        return skill;
    }

    public Category getCategory() {
        return category;
    }

    public void setSkillId(Integer skillId) {
        this.skillId = skillId;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
