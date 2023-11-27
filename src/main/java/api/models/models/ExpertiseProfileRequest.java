package api.models.models;

import api.models.BaseModel;
import com.telerikacademy.testframework.models.SetOfSkills;
import com.telerikacademy.testframework.models.User;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ExpertiseProfileRequest extends BaseModel {
    private Category category;
    private double availability;
    private String skill1;
    private String skill2;
    private String skill3;
    private String skill4;
    private String skill5;


    public ExpertiseProfileRequest(User user) {
        SetOfSkills skills = user.getProfile().getSkills();
        availability = skills.getWeeklyAvailability();
        category = new Category(user.getCategory());
        skill1 = skills.getSkillOne();
        skill2 = skills.getSkillTwo();
        skill3 = skills.getSkillThree();
        skill4 = skills.getSkillFour();
        skill5 = skills.getSkillFive();

    }
}
