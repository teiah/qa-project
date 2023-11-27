package api.models;

import com.telerikacademy.testframework.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserRequest {
    public UserRequest(String authority, User user) {
        authorities = new ArrayList<>();
        addAuthority(authority);
        setCategory(new Category(user.getCategory()));
        setEmail(user.getEmail());
        setPassword(user.getPassword());
        setUsername(user.getUsername());

    }

    private List<String> authorities;
    private String username;
    private String email;
    private String password;
    private Category category;
    private int id;


    private void addAuthority(String string) {
        authorities.add(string);
    }
}
