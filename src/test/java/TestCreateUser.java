import io.restassured.RestAssured;
import io.restassured.response.Response;
import object_api.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.json.JSONObject;
import org.junit.runner.Request;
import step_api.UserStep;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestCreateUser {

    final static String email = "tatuka1001@yandex.ru";
    final static String password = "1234567";
    final static String name = "aske";
    public static String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api/auth";
    }

    @Test
    public void createNewUser(){
        User user = new User(email, password, name);

        Response response = UserStep.createUser(user);
        response.then().assertThat().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
        token = new JSONObject(response.getBody().asString()).get("accessToken").toString().substring(7);

    }

    @Test
    public void notBeTwoTwinsUsers(){
        User user = new User(email, password, name);
        Response response = UserStep.createUser(user);
        response.then().assertThat().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
        token = new JSONObject(response.getBody().asString()).get("accessToken").toString().substring(7);
        UserStep.createUser(user)
                .then().assertThat().statusCode(403)
                .and()
                .assertThat().body("message", equalTo("User already exists"));
    }



    @After
    public void cleanUp() {
       UserStep.deleteUser(token);


    }


}
