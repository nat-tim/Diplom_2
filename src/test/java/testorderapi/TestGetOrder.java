package testorderapi;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import objectapi.Order;
import objectapi.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stepapi.OrderStep;
import stepapi.UserStep;
import setting.SetTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class TestGetOrder {
    static Faker faker = new Faker();
    final static String email = faker.internet().emailAddress();
    final static String password = faker.internet().password(6, 12);
    final static String name = faker.name().firstName();
    public static String token;
    final static List<String> ingredients = new ArrayList<String>();
    @Before
    public void setUp() {
        RestAssured.baseURI = SetTest.getBaseUri();
        User user = new User(email, password, name);
        Response response = UserStep.createUser(user);
        response.then().assertThat().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
        token = user.setGetToken(response);
        List<String> ingredientsId = new ArrayList<String>(OrderStep.getListIngredients());
        Random r = new Random();
        int x = ingredientsId.size();
        ingredients.add(ingredientsId.get(r.nextInt(x)));
        ingredients.add(ingredientsId.get(r.nextInt(x)));
        ingredients.add(ingredientsId.get(r.nextInt(x)));
        Order order = new Order(ingredients);
        Response response1 = OrderStep.createOrder(order, token);
        response1.then().assertThat().statusCode(200)
                .and()
                .assertThat().body("order.owner", notNullValue());
    }

    @Test
    @DisplayName("Get list of user's orders with authorization")
    @Description("This is test which checks getting a list of user's orders, when the user has logged in")
    public void shouldBeGetOrderWithAuthorization(){
        Response response = OrderStep.getOrdersUser(token);
        response
                .then().assertThat().statusCode(200)
                .and()
                .assertThat().body("orders", notNullValue());
    }

    @Test
    @DisplayName("Get list of user's orders without authorization")
    @Description("This is test which checks inability to get a list of user's orders, when the user hasn't logged in")
    public void shouldNotBeGetOrderWithoutAuthorization(){
        Response response = OrderStep.getOrdersUser("");
        response
                .then().assertThat().statusCode(401)
                .and()
                .assertThat().body("message", equalTo("You should be authorised"));

    }

    @After
    public void cleanUp() {
        UserStep.deleteUser(token);
        ingredients.clear();
    }

}
