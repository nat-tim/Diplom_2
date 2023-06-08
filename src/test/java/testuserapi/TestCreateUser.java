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

public class TestCreateUser {

    static Faker faker = new Faker();
    final static String email = faker.internet().emailAddress();
    final static String password = faker.internet().password(6, 12);
    final static String name = faker.name().firstName();
    public static String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = SetTest.getBaseUri();
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
        token = user.setGetToken(response);

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
        token = user.setGetToken(response);
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
