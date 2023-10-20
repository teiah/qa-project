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

    UserModel skillUser = new UserModel();

    @BeforeClass
    public void setUpSkillTest() {
        skillUser.register(ROLE_USER.toString());
    }

    @AfterClass
    public void cleanUpSkillTest() {
        globalRESTAdminUser.disableUser(skillUser.getId());
    }

    @Test
    public void AllSkillsListed_When_Requested_By_User() {

        List<Integer> skillIds = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SkillModel skill = skillUser.createSkill();
            assertTrue(skillExists(skill.getSkillId()), "Skill was not deleted.");
            skillIds.add(skill.getSkillId());
        }

        SkillModel[] foundSkills = getAllSkills();

        assertTrue(foundSkills.length > 0, "There are no skills found.");

        for (SkillModel skill : foundSkills) {
            if (skillIds.contains(skill.getSkillId())) {
                skillUser.deleteSkill(skill.getSkillId());
                assertFalse(skillExists(skill.getSkillId()), "Skill was not deleted.");
            }
        }

    }

    @Test
    public void Skill_Created_By_User() {

        SkillModel skill = skillUser.createSkill();
        assertTrue(skillExists(skill.getSkillId()), "Skill was not deleted.");

        skillUser.deleteSkill(skill.getSkillId());
        assertFalse(skillExists(skill.getSkillId()), "Skill was not deleted.");
    }

    @Test
    public void Skill_Deleted_By_User() {

        SkillModel skill = skillUser.createSkill();
        assertTrue(skillExists(skill.getSkillId()), "Skill was not deleted.");

        skillUser.deleteSkill(skill.getSkillId());
        assertFalse(skillExists(skill.getSkillId()), "Skill was not deleted.");

    }

    @Test
    public void Skill_Edited_By_User() {

        SkillModel skill = skillUser.createSkill();
        assertTrue(skillExists(skill.getSkillId()), "Skill was not deleted.");

        Response editedSkillResponse = skillUser.editSkill(skill.getSkillId());

        int statusCode = editedSkillResponse.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        skillUser.deleteSkill(skill.getSkillId());
        assertFalse(skillExists(skill.getSkillId()), "Skill was not deleted.");
    }

    @Test
    public void SkillFoundById_When_Requested_By_User() {

        SkillModel skill = skillUser.createSkill();
        assertTrue(skillExists(skill.getSkillId()), "Skill was not deleted.");

        SkillModel foundSkill = skillUser.getSkillById(skill.getSkillId());

        assertEquals(foundSkill.getSkillId(), skill.getSkillId(), "Ids do not match.");
        assertNotNull(foundSkill, "Skill not found.");

        skillUser.deleteSkill(skill.getSkillId());
        assertFalse(skillExists(skill.getSkillId()), "Skill was not deleted.");
    }

}