package test_order_api;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import object_api.Order;
import object_api.User;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import step_api.OrderStep;
import step_api.UserStep;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestCreateOrder {
    final static String email = "tatuka1001@yandex.ru";
    final static String password = "1234567";
    final static String name = "naske";
    public static String token;
    final static List<String> ingredients = new ArrayList<String>();
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
        ingredients.add("60d6a733c6");
        ingredients.add("6c916e00276b2870");
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
