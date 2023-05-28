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

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class TestLoginUserParametrized {

    final static String email = "tatuka1001@yandex.ru";
    final static String password = "1234567";
    final static String name = "aske";

    public static String token;
    private final String emailParam;
    private final String passwordParam;



    public TestLoginUserParametrized(String emailParam, String passwordParam) {
        this.emailParam = emailParam;
        this.passwordParam = passwordParam;
    }

    @Parameterized.Parameters
    public static Object[][] getCredentials() {
        return new Object[][]{
                {"tatuka1001@yandex.ru", "123"},
                {"tatuka100500@yandex.ru", "1234567"},
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
    @DisplayName("Login user with incorrect parameters")
    @Description("This is test checks inability to login user, when parameters is incorrect")
    public void shouldNotBeLoginUserWithIncorrectParameters(){
        User user = new User(emailParam, passwordParam);
        Response response = UserStep.loginUser(user);
        response.then().assertThat().statusCode(401)
                .and()
                .assertThat().body("message", equalTo("email or password are incorrect"));

    }

    @After
    public void cleanUp() {
        UserStep.deleteUser(token);
    }
}
