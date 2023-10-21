package test.cases.wearerestassuredtests.tests;

import models.models.SkillModel;
import models.models.UserModel;
import io.restassured.response.Response;
import org.testng.annotations.*;
import test.cases.wearerestassuredtests.base.BaseWeareRestAssuredTest;

import java.util.ArrayList;
import java.util.List;

import static models.basemodel.BaseModel.*;
import static com.telerikacademy.testframework.utils.UserRoles.ROLE_USER;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.*;

public class RESTSkillControllerTest extends BaseWeareRestAssuredTest {

    @Test
    public void AllSkillsListed_When_Requested_By_User() {

        List<Integer> skillIds = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SkillModel skill = globalRestApiUser.createSkill();
            assertTrue(skillExists(skill.getSkillId()), "Skill was not deleted.");
            skillIds.add(skill.getSkillId());
        }

        SkillModel[] foundSkills = getAllSkills();

        assertTrue(foundSkills.length > 0, "There are no skills found.");

        for (SkillModel skill : foundSkills) {
            if (skillIds.contains(skill.getSkillId())) {
                globalRestApiUser.deleteSkill(skill.getSkillId());
                assertFalse(skillExists(skill.getSkillId()), "Skill was not deleted.");
            }
        }

    }

    @Test
    public void Skill_Created_By_User() {

        SkillModel skill = globalRestApiUser.createSkill();
        assertTrue(skillExists(skill.getSkillId()), "Skill was not deleted.");

        globalRestApiUser.deleteSkill(skill.getSkillId());
        assertFalse(skillExists(skill.getSkillId()), "Skill was not deleted.");
    }

    @Test
    public void Skill_Deleted_By_User() {

        SkillModel skill = globalRestApiUser.createSkill();
        assertTrue(skillExists(skill.getSkillId()), "Skill was not deleted.");

        globalRestApiUser.deleteSkill(skill.getSkillId());
        assertFalse(skillExists(skill.getSkillId()), "Skill was not deleted.");

    }

    @Test
    public void Skill_Edited_By_User() {

        SkillModel skill = globalRestApiUser.createSkill();
        assertTrue(skillExists(skill.getSkillId()), "Skill was not deleted.");

        Response editedSkillResponse = globalRestApiUser.editSkill(skill.getSkillId());

        int statusCode = editedSkillResponse.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        globalRestApiUser.deleteSkill(skill.getSkillId());
        assertFalse(skillExists(skill.getSkillId()), "Skill was not deleted.");
    }

    @Test
    public void SkillFoundById_When_Requested_By_User() {

        SkillModel skill = globalRestApiUser.createSkill();
        assertTrue(skillExists(skill.getSkillId()), "Skill was not deleted.");

        SkillModel foundSkill = globalRestApiUser.getSkillById(skill.getSkillId());

        assertEquals(foundSkill.getSkillId(), skill.getSkillId(), "Ids do not match.");
        assertNotNull(foundSkill, "Skill not found.");

        globalRestApiUser.deleteSkill(skill.getSkillId());
        assertFalse(skillExists(skill.getSkillId()), "Skill was not deleted.");
    }

}