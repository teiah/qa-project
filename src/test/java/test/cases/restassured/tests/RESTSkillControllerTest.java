//package test.cases.restassured.tests;
//
//import api.models.Skill;
//import io.restassured.response.Response;
//import org.testng.annotations.*;
//import api.controllers.SkillController;
//import test.cases.restassured.base.BaseWeareRestAssuredTest;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.apache.http.HttpStatus.SC_OK;
//import static org.testng.Assert.*;
//
//public class RESTSkillControllerTest extends BaseWeareRestAssuredTest {
//
//    @Test
//    public void allSkillsListed_When_Requested_By_User() {
//
//        List<Integer> skillIds = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            Skill skill = SkillController.createSkill(globalRestApiUser);
//            assertTrue(SkillController.skillExists(skill.getSkillId()), "Skill was not deleted.");
//            skillIds.add(skill.getSkillId());
//        }
//
//        Skill[] foundSkills = SkillController.getAllSkills();
//
//        assertTrue(foundSkills.length > 0, "There are no skills found.");
//
//        for (Skill skill : foundSkills) {
//            if (skillIds.contains(skill.getSkillId())) {
//                SkillController.deleteSkill(skill.getSkillId());
//                assertFalse(SkillController.skillExists(skill.getSkillId()), "Skill was not deleted.");
//            }
//        }
//
//    }
//
//    @Test
//    public void skill_Created_By_User() {
//
//        Skill skill = SkillController.createSkill(globalRestApiUser);
//        assertTrue(SkillController.skillExists(skill.getSkillId()), "Skill was not deleted.");
//
//        SkillController.deleteSkill(skill.getSkillId());
//        assertFalse(SkillController.skillExists(skill.getSkillId()), "Skill was not deleted.");
//    }
//
//    @Test
//    public void skill_Deleted_By_User() {
//
//        Skill skill = SkillController.createSkill(globalRestApiUser);
//        assertTrue(SkillController.skillExists(skill.getSkillId()), "Skill was not deleted.");
//
//        SkillController.deleteSkill(skill.getSkillId());
//        assertFalse(SkillController.skillExists(skill.getSkillId()), "Skill was not deleted.");
//
//    }
//
//    @Test
//    public void skill_Edited_By_User() {
//
//        Skill skill = SkillController.createSkill(globalRestApiUser);
//        assertTrue(SkillController.skillExists(skill.getSkillId()), "Skill was not deleted.");
//
//        Response editedSkillResponse = SkillController.editSkill(skill.getSkillId());
//
//        int statusCode = editedSkillResponse.getStatusCode();
//        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");
//
//        SkillController.deleteSkill(skill.getSkillId());
//        assertFalse(SkillController.skillExists(skill.getSkillId()), "Skill was not deleted.");
//    }
//
//    @Test
//    public void skillFoundById_When_Requested_By_User() {
//
//        Skill skill = SkillController.createSkill(globalRestApiUser);
//        assertTrue(SkillController.skillExists(skill.getSkillId()), "Skill was not deleted.");
//
//        Skill foundSkill = SkillController.getSkillById(skill.getSkillId());
//
//        assertEquals(foundSkill.getSkillId(), skill.getSkillId(), "Ids do not match.");
//        assertNotNull(foundSkill, "Skill not found.");
//
//        SkillController.deleteSkill(skill.getSkillId());
//        assertFalse(SkillController.skillExists(skill.getSkillId()), "Skill was not deleted.");
//    }
//
//}