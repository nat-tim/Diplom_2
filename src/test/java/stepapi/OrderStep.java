package stepapi;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import objectapi.Order;
import setting.SetTest;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderStep {
    final static String orderApi = "/orders";

    @Step("get list of existing ingredients")
    public static List<String> getListIngredients(){
        Response response= responseListIngredients();
        ListIngredients listIngredients = response.body().as(ListIngredients.class);
        List<String> ingredientsId = new ArrayList<>(listIngredients.getData().size());
        for (int i = 0; i < listIngredients.getData().size(); i++){
            String id = listIngredients.getData().get(i).get_id();
            ingredientsId.add(id);
        }
        return ingredientsId;
    }
    @Step("response list of existing ingredients")
    public static Response responseListIngredients() {
        Response response= given()
                .header("Content-type", "application/json")
                .when()
                .get(SetTest.getBaseUri() + "/ingredients");
        response.then().assertThat().statusCode(200);
        return response;

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
    class ListIngredients{

        private List<IdIngredient> data;

        public List<IdIngredient> getData() {
            return data;
        }

        public void setData(List<IdIngredient> data) {
            this.data = data;
        }
    }
    class IdIngredient{

        private String _id;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }
    }
}
