package testuserapi;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import objectapi.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import setting.SetTest;
import stepapi.UserStep;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class TestCreateUserParametrized {
    static Faker faker = new Faker();
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
                {faker.internet().emailAddress(), faker.internet().password(6, 12), ""},
                {faker.internet().emailAddress(), "", faker.name().firstName()},
                {"", faker.internet().password(6, 12), faker.name().firstName()},
        };
    }



    @Before
    public void setUp() {
        RestAssured.baseURI = SetTest.getBaseUri();
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
