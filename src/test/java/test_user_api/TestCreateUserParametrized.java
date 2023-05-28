package test_user_api;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import object_api.User;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import step_api.UserStep;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class TestCreateUserParametrized {

    private final String emailParam;
    private final String passwordParam;
    private final String userNameParam;


    public TestCreateUserParametrized(String emailParam, String passwordParam, String userNameParam) {
        this.emailParam = emailParam;
        this.passwordParam = passwordParam;
        this.userNameParam = userNameParam;
    }

    @Parameterized.Parameters
    public static Object[][] getCredentials() {
        return new Object[][]{
                {"tatuka100500@yandex.ru", "123", ""},
                {"tatuka100500@yandex.ru", "", "user"},
                {"", "123", "user"},
        };
    }



    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api";
    }

    @Test
    @DisplayName("Create user with incorrect parameters")
    @Description("This is test checks inability to create user, when parameters is incorrect")
    public void shouldNotBeCreateNewUserWithIncorrectParameters(){
        User user = new User(emailParam, passwordParam, userNameParam);

        Response response = UserStep.createUser(user);
        response.then().assertThat().statusCode(403)
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));

    }
}
