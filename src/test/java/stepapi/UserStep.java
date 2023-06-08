package stepapi;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import objectapi.User;

import static io.restassured.RestAssured.given;

public class UserStep {
    final static String createApi = "/auth/register";
    final static String loginApi = "/auth/login";
    final static String deleteAndChangeApi = "/auth/user";


    @Step("create user")
    public static Response createUser(User user) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(createApi);
        return response;

    }


    @Step("login user")
    public static Response loginUser(User user) {

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(loginApi)
                .then().extract().response();
        return response;
    }

    @Step("change user's parameters with authorization")
    public static Response changeWithLoginUser(User user, String token) {

        Response response = given()
                .header("Content-type", "application/json")
                .auth()
                .oauth2(token)
                .and()
                .body(user)
                .when()
                .patch(deleteAndChangeApi);
        return response;
    }

    @Step("change user's parameters without authorization")
    public static Response changeWithoutLoginUser(User user) {

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .patch(deleteAndChangeApi);
        return response;
    }

    @Step("delete User")
    public static void deleteUser(String token) {

        given()
                .header("Content-type", "application/json")
                .auth()
                .oauth2(token)
                .when()
                .delete(deleteAndChangeApi)
                .then().assertThat().statusCode(202);
    }


}
