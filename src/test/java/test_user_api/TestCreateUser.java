package test_user_api;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
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
    final static String name = "naske";
    public static String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api";
    }

    @Test
    @DisplayName("Create new user")
    @Description("This is test checks ability to create new user, when parameters are correct")
    public void shouldBeCreateNewUserWithCorrectParameters(){
        User user = new User(email, password, name);

        Response response = UserStep.createUser(user);
        response.then().assertThat().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
        token = new JSONObject(response.getBody().asString()).get("accessToken").toString().substring(7);

    }

    @Test
    @DisplayName("Create new twins user")
    @Description("This is test checks inability to create new user, when parameters are incorrect and there is a twin ")
    public void shouldNotBeTwoTwinsUsers(){
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
