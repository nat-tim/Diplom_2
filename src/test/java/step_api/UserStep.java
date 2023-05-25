package step_api;

import io.restassured.response.Response;
import object_api.User;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class UserStep {
    final static String createApi = "/register";
    final static String loginApi = "/login";
   //final static String delApi = "/api/v1/courier/";


    public static Response createUser(User user) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(createApi);
        return response;

    }


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



    public static void deleteUser(String token) {

        given()
                .header("Content-type", "application/json")
                .auth()
                .oauth2(token)
                .when()
                .delete("https://stellarburgers.nomoreparties.site/api/auth/user")
                .then().assertThat().statusCode(202);
    }


}
