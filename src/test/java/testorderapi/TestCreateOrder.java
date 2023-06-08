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

public class TestCreateOrder {
    static Faker faker = new Faker();
    final static String email = faker.internet().emailAddress();
    final static String password = faker.internet().password(6, 12);
    final static String name = faker.name().firstName();
    final static String incorrectNumIngredient1 = faker.bothify("#???######????");
    final static String incorrectNumIngredient2 = faker.bothify("#???######????");
    public String token;
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
    }

    @Test
    @DisplayName("Create order with authorization and correct ingredients")
    @Description("This is test checks ability to create orders, when the user has logged in and ingredients is correct")
    public void shouldBeCreateOrderWithAuthorizationAndCorrectIngredients(){
        List<String> ingredientsId = new ArrayList<String>(OrderStep.getListIngredients());
        Random r = new Random();
        int x = ingredientsId.size();
        ingredients.add(ingredientsId.get(r.nextInt(x)));
        ingredients.add(ingredientsId.get(r.nextInt(x)));
        ingredients.add(ingredientsId.get(r.nextInt(x)));
        Order order = new Order(ingredients);
        Response response= OrderStep.createOrder(order, token);
        response.then().assertThat().statusCode(200)
                .and()
                .assertThat().body("order.owner", notNullValue());

    }

    @Test
    @DisplayName("Create order without authorization")
    @Description("This is test checks ability to create orders, when the user hasn't logged in and ingredients is correct")
    public void shouldNotBeCreateOrderWithoutAuthorizationAndCorrectIngredients(){
        List<String> ingredientsId = new ArrayList<String>(OrderStep.getListIngredients());
        Random r = new Random();
        int x = ingredientsId.size();
        ingredients.add(ingredientsId.get(r.nextInt(x)));
        ingredients.add(ingredientsId.get(r.nextInt(x)));
        ingredients.add(ingredientsId.get(r.nextInt(x)));
        Order order = new Order(ingredients);
        Response response= OrderStep.createOrder(order, "");
        response.then().assertThat().statusCode(200)
                .and()
                .assertThat().body("order.owner", nullValue());

    }

    @Test
    @DisplayName("Create order with authorization and incorrect ingredients")
    @Description("This is test checks ability to create orders, when the user has logged in and ingredients isn't correct")
    public void shouldNotBeCreateOrderWithAuthorizationAndNotCorrectIngredients() {
        ingredients.add(incorrectNumIngredient1);
        ingredients.add(incorrectNumIngredient2);
        Order order = new Order(ingredients);
        Response response = OrderStep.createOrder(order, token);
        response.then().assertThat().statusCode(500);
    }

    @Test
    @DisplayName("Create order with authorization and without ingredients")
    @Description("This is test checks ability to create orders, when the user has logged in and no ingredients")
    public void shouldNotBeCreateOrderWithAuthorizationAndWithoutIngredients() {
        Order order = new Order(ingredients);
        Response response = OrderStep.createOrder(order, token);
        response.then().assertThat().statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Ingredient ids must be provided"));
    }

    @After
    public void cleanUp() {
        UserStep.deleteUser(token);
        ingredients.clear();
    }
}
