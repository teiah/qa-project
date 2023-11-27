package api.models;

import com.telerikacademy.testframework.utils.Expertise;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Skill extends BaseModel {
    public Skill(String skill, Expertise category) {
        setSkillId(0);
        setSkill(skill);
        setCategory(new Category(category));
    }

    private int skillId;
    private String skill;
    private Category category;
}
