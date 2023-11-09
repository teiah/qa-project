package factories;

import api.models.models.User;
import com.telerikacademy.testframework.utils.Authority;
import com.telerikacademy.testframework.utils.Helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.telerikacademy.testframework.utils.Authority.ROLE_USER;
import static com.telerikacademy.testframework.utils.Authority.ROLE_ADMIN;

public class UserFactory {


    public static User createUser() {
        int categoryId = Helpers.generateCategoryId();
        String username = Helpers.generateUsername(ROLE_USER.toString());
        List<Authority> role = Collections.singletonList(ROLE_USER);
        String email = Helpers.generateEmail();
        String password = Helpers.generatePassword();
        return new User(categoryId, username, role, email, password, password);
    }

    public static User createAdmin() {
        int categoryId = Helpers.generateCategoryId();
        String username = Helpers.generateUsername(ROLE_ADMIN.toString());
        List<Authority> role = new ArrayList<Authority>() {{
            add(ROLE_USER);
            add(ROLE_ADMIN);
        }};
        String email = Helpers.generateEmail();
        String password = Helpers.generatePassword();
        return new User(categoryId, username, role, email, password, password);
    }
}
