package factories;

import com.telerikacademy.testframework.models.SetOfSkills;
import com.telerikacademy.testframework.utils.Helpers;


public class ExpertiseFactory {

    public static SetOfSkills createSkills() {
        SetOfSkills skills = new SetOfSkills();
        skills.setSkillOne(Helpers.generateSkill());
        skills.setSkillTwo(Helpers.generateSkill());
        skills.setSkillThree(Helpers.generateSkill());
        skills.setSkillFour(Helpers.generateSkill());
        skills.setSkillFive(Helpers.generateSkill());
        skills.setWeeklyAvailability(Helpers.generateAvailability());

        return skills;
    }


}
