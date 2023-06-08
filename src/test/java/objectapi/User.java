package objectapi;

import io.restassured.response.Response;
import stepapi.OrderStep;

public class User {

    private String email;
    private String password;
    private String name;

    private String token;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.token = null;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.name = null;
        this.token = null;
    }

    public User() {

    }
    public String getToken() {
        return token;
    }
    public String setGetToken(Response response) {
        this.token = response.body().as(UserToken.class).getAccessToken().substring(7);
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public void setToken(Response response) {
        this.token = response.body().as(UserToken.class).getAccessToken().substring(7);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }
    class UserToken{

        private String accessToken;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
    }
}
