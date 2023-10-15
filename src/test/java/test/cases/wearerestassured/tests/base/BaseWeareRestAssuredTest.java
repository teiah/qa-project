package test.cases.wearerestassured.tests.base;

import org.testng.annotations.AfterClass;
import test.cases.BaseTestSetup;


public class BaseWeareRestAssuredTest extends BaseTestSetup {

    @AfterClass
    public void tearDownRestAssured() {
        WEareApi.disableUser(globalAdminUser, globalAdminUser.getId());
    }

}
