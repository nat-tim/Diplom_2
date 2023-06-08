package testuserapi;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import objectapi.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import setting.SetTest;
import stepapi.UserStep;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class TestLoginUserParametrized {

    static Faker faker = new Faker();
    final static String email = faker.internet().emailAddress();
    final static String password = faker.internet().password(6, 12);
    final static String name = faker.name().firstName();

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
                {email, faker.internet().password(6, 12)},
                {faker.internet().emailAddress(), password},
        };
    }



    @Before
    public void setUp() {
        RestAssured.baseURI = SetTest.getBaseUri();
        User user = new User(email, password, name);
        Response response = UserStep.createUser(user);
        response.then().assertThat().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
        token = user.setGetToken(response);
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
