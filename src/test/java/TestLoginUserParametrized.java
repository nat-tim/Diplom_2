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


    /*
        @Test
        @DisplayName("Check no way to login courier with invalid param")
        @Description("This is test which checks no way to login courier with invalid param")

     */
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api/auth";
        User user = new User(email, password, name);
        Response response = UserStep.createUser(user);
        response.then().assertThat().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
        token = new JSONObject(response.getBody().asString()).get("accessToken").toString().substring(7);
    }

    @Test
    public void loginUser(){
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
