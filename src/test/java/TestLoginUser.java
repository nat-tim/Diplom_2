import io.restassured.RestAssured;
import io.restassured.response.Response;
import object_api.User;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import step_api.UserStep;

import static org.hamcrest.Matchers.equalTo;

public class TestLoginUser {
    final static String email = "tatuka1001@yandex.ru";
    final static String password = "1234567";
    final static String name = "aske";

    public static String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api/auth";
        User user = new User(email, password, name);
        Response response = UserStep.createUser(user);
        response.then().assertThat().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    public void loginUser(){
        User user = new User(email, password);
        Response response = UserStep.loginUser(user);
        response.then().assertThat().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
        token = new JSONObject(response.getBody().asString()).get("accessToken").toString().substring(7);

    }

    @After
    public void cleanUp() {
        UserStep.deleteUser(token);
    }

}
