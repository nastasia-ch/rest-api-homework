package testsFromLesson.tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class ReqresInTestsLesson {

    /*
    1. Сделать запрос на https://reqres.in/api/login
    с телом { "email": "eve.holt@reqres.in", "password": "cityslicka" }
    2. Получить ответ { "token": "QpwL5tke4Pnpja7X4" }
    3. Проверить token is QpwL5tke4Pnpja7X4
    */

    @Test
    void loginTest() {

        String data = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }";
        given()
                .log().uri()
                .contentType(JSON) // обязательно нужно указать тип передаваемых данных
                .body(data)
        .when()
                .post("https://reqres.in/api/login")
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));

    }

    @Test
    void unSupportedMediaTypeTest() {

        given()
                .log().uri()
        .when()
                .post("https://reqres.in/api/login")
        .then()
                .log().status()
                .log().body()
                .statusCode(415);

    }

    @Test
    void missingEmailOrPasswordTest() {

        given()
                .log().uri()
                .body("123")
        .when()
                .post("https://reqres.in/api/login")
        .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));

    }

    @Test
    void missingPasswordTest() {

        String data = "{ \"email\": \"eve.holt@reqres.in\"}";
        given()
                .log().uri()
                .contentType(JSON) // обязательно нужно указать тип передаваемых данных
                .body(data)
        .when()
                .post("https://reqres.in/api/login")
        .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));

    }

}
