package step_api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import object_api.Order;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderStep {
    final static String orderApi = "/orders";

    @Step("get list of existing ingridients")
    public static List<String> getListIngredients(){
        Response response= given()
                .header("Content-type", "application/json")
                .when()
                .get("https://stellarburgers.nomoreparties.site/api/ingredients");
        response.then().assertThat().statusCode(200);
        JSONArray tempArray = new JSONObject(response.getBody().asString()).getJSONArray("data");
        List<String> ingredientsId = new ArrayList<>(tempArray.length());
        for (int i = 0; i < tempArray.length(); i++){
            JSONObject obj = tempArray.getJSONObject(i);
            ingredientsId.add(obj.getString("_id"));
        }
        return ingredientsId;

    }

    @Step("create orders")
    public static Response createOrder(Order order, String token) {
        Response response= given()
                .header("Content-type", "application/json")
                .auth()
                .oauth2(token)
                .and()
                .body(order)
                .when()
                .post(orderApi);
        return response;

    }

    @Step("get user's orders")
    public static Response getOrdersUser(String token) {
        Response response = given()
                .header("Content-type", "application/json")
                .auth()
                .oauth2(token)
                .when()
                .get(orderApi);
        return response;

    }
}
