package test.cases.wearerestassured.tests;

import com.telerikacademy.testframework.models.SkillModel;
import com.telerikacademy.testframework.models.UserModel;
import io.restassured.response.Response;
import org.testng.annotations.*;
import test.cases.BaseTestSetup;

import java.util.ArrayList;
import java.util.List;

import static com.telerikacademy.testframework.utils.UserRoles.*;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.*;

public class RESTSkillControllerTest extends BaseTestSetup {

    private UserModel skillUser;

    @BeforeClass
    private void setUp() {
        skillUser = weareApi.registerUser(ROLE_USER.toString());
    }

    @AfterClass
    public void clear() {
        weareApi.disableUser(globalAdminUser, skillUser.getId());
    }

    @Test
    public void userCanFindAllSkills() {

        List<Integer> skillIds = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SkillModel skill = weareApi.createSkill(skillUser);
            skillIds.add(skill.getSkillId());
        }

        SkillModel[] foundSkills = weareApi.getAllSkills();

        assertTrue(foundSkills.length > 0, "There are no skills found.");

        for (SkillModel skill : foundSkills) {
            if (skillIds.contains(skill.getSkillId())) {
                weareApi.deleteSkill(skill.getSkillId());
            }
        }

    }

    @Test
    public void userCanCreateSkill() {

        SkillModel skill = weareApi.createSkill(skillUser);

        assertNotNull(skill, "Skill was not created.");

        weareApi.deleteSkill(skill.getSkillId());
    }

    @Test
    public void userCanDeleteSkill() {

        SkillModel skill = weareApi.createSkill(skillUser);

        weareApi.deleteSkill(skill.getSkillId());

        assertFalse(weareApi.skillExists(skill.getSkillId()), "Skill was not deleted.");

    }

    @Test
    public void userCanEditSkill() {

        SkillModel skill = weareApi.createSkill(skillUser);

        Response editedSkillResponse = weareApi.editSkill(skill.getSkillId());

        int statusCode = editedSkillResponse.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        weareApi.deleteSkill(skill.getSkillId());
    }

    @Test
    public void userCanGetSkillById() {

        SkillModel skill = weareApi.createSkill(skillUser);

        SkillModel foundSkill = weareApi.getSkillById(skill.getSkillId());

        assertEquals(foundSkill.getSkillId(), skill.getSkillId(), "Ids do not match.");
        assertNotNull(foundSkill, "Skill not found.");

        weareApi.deleteSkill(skill.getSkillId());

    }

}