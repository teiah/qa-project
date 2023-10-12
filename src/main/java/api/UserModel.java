package api;

import com.telerikacademy.testframework.Utils;
import io.restassured.response.Response;

public class UserModel {
    private String id;
    private String username;
    private String email;
    private String password;


    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setUserId(String userId) {
        this.id = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
