package factories;

import api.models.models.User;
import com.telerikacademy.testframework.utils.Authority;
import com.telerikacademy.testframework.utils.Helpers;

import java.util.Collections;
import java.util.List;

import static com.telerikacademy.testframework.utils.Authority.ROLE_USER;

public class UserFactory {


    public static User createUser(){
        int categoryId = Helpers.generateCategoryId();
        String username = Helpers.generateFirstName();
        List<Authority> role = Collections.singletonList(ROLE_USER);
        String email = Helpers.generateEmail();
        String password = Helpers.generatePassword();
        return new User(categoryId, username, role, email, password, password);
    }
}
