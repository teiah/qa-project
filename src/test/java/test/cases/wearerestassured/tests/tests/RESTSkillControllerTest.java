package test.cases.wearerestassured.tests.tests;

import com.telerikacademy.testframework.models.SkillModel;
import com.telerikacademy.testframework.models.UserModel;
import io.restassured.response.Response;
import org.testng.annotations.*;
import test.cases.BaseTestSetup;
import test.cases.wearerestassured.tests.base.BaseWeareRestAssuredTest;

import java.util.ArrayList;
import java.util.List;

import static com.telerikacademy.testframework.utils.UserRoles.*;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.*;

public class RESTSkillControllerTest extends BaseWeareRestAssuredTest {

    private UserModel skillUser;

    @BeforeClass
    public void setUp() {
        skillUser = WEareApi.registerUser(ROLE_USER.toString());
    }

    @AfterClass
    public void clear() {
        WEareApi.disableUser(globalAdminUser, skillUser.getId());
    }

    @Test
    public void userCanFindAllSkills() {

        List<Integer> skillIds = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SkillModel skill = WEareApi.createSkill(skillUser);
            skillIds.add(skill.getSkillId());
        }

        SkillModel[] foundSkills = WEareApi.getAllSkills();

        assertTrue(foundSkills.length > 0, "There are no skills found.");

        for (SkillModel skill : foundSkills) {
            if (skillIds.contains(skill.getSkillId())) {
                WEareApi.deleteSkill(skill.getSkillId());
            }
        }

    }

    @Test
    public void userCanCreateSkill() {

        SkillModel skill = WEareApi.createSkill(skillUser);

        assertNotNull(skill, "Skill was not created.");

        WEareApi.deleteSkill(skill.getSkillId());
    }

    @Test
    public void userCanDeleteSkill() {

        SkillModel skill = WEareApi.createSkill(skillUser);

        WEareApi.deleteSkill(skill.getSkillId());

        assertFalse(WEareApi.skillExists(skill.getSkillId()), "Skill was not deleted.");

    }

    @Test
    public void userCanEditSkill() {

        SkillModel skill = WEareApi.createSkill(skillUser);

        Response editedSkillResponse = WEareApi.editSkill(skill.getSkillId());

        int statusCode = editedSkillResponse.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        WEareApi.deleteSkill(skill.getSkillId());
    }

    @Test
    public void userCanGetSkillById() {

        SkillModel skill = WEareApi.createSkill(skillUser);

        SkillModel foundSkill = WEareApi.getSkillById(skill.getSkillId());

        assertEquals(foundSkill.getSkillId(), skill.getSkillId(), "Ids do not match.");
        assertNotNull(foundSkill, "Skill not found.");

        WEareApi.deleteSkill(skill.getSkillId());

    }

}