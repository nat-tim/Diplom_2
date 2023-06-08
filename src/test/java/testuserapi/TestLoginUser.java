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
import setting.SetTest;
import stepapi.UserStep;

import static org.hamcrest.Matchers.equalTo;

public class TestLoginUser {
    static Faker faker = new Faker();
    final static String email = faker.internet().emailAddress();//"tatuka1001@yandex.ru";
    final static String password = faker.internet().password(6, 12);//"1234567";
    final static String name = faker.name().firstName();

    public static String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = SetTest.getBaseUri();
        User user = new User(email, password, name);
        Response response = UserStep.createUser(user);
        response.then().assertThat().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Login user with correct parameters")
    @Description("This is test checks ability to login user, when parameters is correct")
    public void shouldBeLoginUserWithCorrectParameters() {
        User user = new User(email, password);
        Response response = UserStep.loginUser(user);
        response.then().assertThat().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
        token = user.setGetToken(response);

    }

    @After
    public void cleanUp() {
        UserStep.deleteUser(token);
    }

}
