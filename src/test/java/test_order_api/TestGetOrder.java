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
import static org.hamcrest.Matchers.equalTo;

public class TestGetOrder {
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
