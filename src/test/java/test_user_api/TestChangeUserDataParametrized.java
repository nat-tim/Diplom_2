package test_user_api;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import object_api.User;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import step_api.UserStep;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class TestChangeUserDataParametrized {
    final static String email = "tatuka1001@yandex.ru";
    final static String password = "1234567";
    final static String name = "aske";

    public static String token;
    private final String emailParam;
    private final String nameParam;



    public TestChangeUserDataParametrized(String emailParam, String nameParam) {
        this.emailParam = emailParam;
        this.nameParam = nameParam;
    }

    @Parameterized.Parameters
    public static Object[][] getCredentials() {
        return new Object[][]{
                {"tatuka1001@yandex.ru", "laymbda"},
                {"tatuka100500@yandex.ru", "aske"},
                {"tatuka100500@yandex.ru", "laymbda"},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api";
        User user = new User(email, password, name);
        Response response = UserStep.createUser(user);
        response.then().assertThat().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
        token = new JSONObject(response.getBody().asString()).get("accessToken").toString().substring(7);
    }

    @Test
    @DisplayName("Change user's parameters with the user has logged in")
    @Description("This is test checks ability to change user's parameters, when he user has logged in")
    public void shouldBeChangedWithLoginUser(){
        User user = new User(emailParam, password, nameParam);
        Response response= UserStep.changeWithLoginUser(user, token);
        response
                .then().assertThat().statusCode(200)
                .and()
                .assertThat().body("user.email", equalTo(emailParam))
                .and()
                .assertThat().body("user.name", equalTo(nameParam));

    }

    @Test
    @DisplayName("Change user's parameters with the user hasn't logged in")
    @Description("This is test checks inability to change user's parameters, when he user hasn't logged in")
    public void shouldNotBeChangedWithoutLoginUser(){
        User user = new User(emailParam, password, nameParam);
        Response response= UserStep.changeWithoutLoginUser(user);
        response
                .then().assertThat().statusCode(401)
                .and()
                .assertThat().body("message", equalTo("You should be authorised"));

    }

    @After
    public void cleanUp() {
        UserStep.deleteUser(token);
    }

}
