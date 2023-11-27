package com.telerikacademy.testframework.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SetOfSkills {

    private String skillOne;
    private String skillTwo;
    private String skillThree;
    private String skillFour;
    private String skillFive;
    private double weeklyAvailability;


    public SetOfSkills(String[] skills) {
        if (skills.length > 5) throw new IllegalArgumentException("Too many services, conversion impossible.");
        try {
            setSkillOne(skills[0]);
            setSkillTwo(skills[1]);
            setSkillThree(skills[2]);
            setSkillFour(skills[3]);
            setSkillFive(skills[4]);
        } catch (ArrayIndexOutOfBoundsException e) {
            // ignore
        }
    }
}
