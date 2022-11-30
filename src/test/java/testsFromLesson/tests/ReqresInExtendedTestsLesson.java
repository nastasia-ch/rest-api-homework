package testsFromLesson.tests;

import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.jupiter.api.Test;
import testsFromLesson.models.lombok.LoginBodyLombokModel;
import testsFromLesson.models.lombok.LoginResponseLombokModel;
import testsFromLesson.models.pojo.LoginBodyPojoModel;
import testsFromLesson.models.pojo.LoginResponsePojoModel;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static testsFromLesson.helpers.CustomApiListener.withCustomTemplates;
import static testsFromLesson.specs.LoginSpecs.loginRequestSpec;
import static testsFromLesson.specs.LoginSpecs.loginResponseSpec;

public class ReqresInExtendedTestsLesson {

    @Test
    void loginTest() {
        //BAD PRACTICE -> move body to model
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
    void loginWithPojoModelTest() {

        // создаем тело запроса в виде экземаляра класса (модель POJO)
        LoginBodyPojoModel body = new LoginBodyPojoModel();  // создаем экземпляр класса
        body.setEmail("eve.holt@reqres.in"); // присваиваем значение полю экземпляра класса email
        body.setPassword("cityslicka");      // присваиваем значение полю экземпляра класса password

        LoginResponsePojoModel response =
        given()
                .log().all()
                .contentType(JSON) // обязательно нужно указать тип передаваемых данных
                .body(body) // в параметр передаем созданный экземпляр класса
        .when()
                .post("https://reqres.in/api/login")
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(LoginResponsePojoModel.class);

        assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");

        // .body("token", is("QpwL5tke4Pnpja7X4")); - встроенный парсер библиотеки rest-assured
        // если мало данных в ответе, можно использовать, если много - то использовать модель POJO для парсинга ответа

    }

    @Test
    void loginWithLombokModelTest() {

        // создаем тело запроса в виде экземпляра класса (модель Lombok)
        LoginBodyLombokModel body = new LoginBodyLombokModel();  // создаем экземпляр класса
        body.setEmail("eve.holt@reqres.in"); // присваиваем значение полю экземпляра класса email
        body.setPassword("cityslicka");      // присваиваем значение полю экземпляра класса password

        LoginResponseLombokModel response =
                given()
                        .log().all()
                        .contentType(JSON) // обязательно нужно указать тип передаваемых данных
                        .body(body) // в параметр передаем созданный экземпляр класса
                        .when()
                        .post("https://reqres.in/api/login")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        .extract().as(LoginResponseLombokModel.class);

        assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");


    }

    @Test
    void loginWithAllureListenerTest() {

        // создаем тело запроса в виде экземпляра класса (модель Lombok)
        LoginBodyLombokModel body = new LoginBodyLombokModel();  // создаем экземпляр класса
        body.setEmail("eve.holt@reqres.in"); // присваиваем значение полю экземпляра класса email
        body.setPassword("cityslicka");      // присваиваем значение полю экземпляра класса password

        LoginResponseLombokModel response =
                given()
                        .filter(new AllureRestAssured()) // добавили Allure Listener
                        .log().all()
                        .contentType(JSON) // обязательно нужно указать тип передаваемых данных
                        .body(body) // в параметр передаем созданный экземпляр класса
                        .when()
                        .post("https://reqres.in/api/login")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        .extract().as(LoginResponseLombokModel.class);

        assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");


    }

    @Test
    void loginWithCustomAllureListenerTest() {

        // создаем тело запроса в виде экземпляра класса (модель Lombok)
        LoginBodyLombokModel body = new LoginBodyLombokModel();  // создаем экземпляр класса
        body.setEmail("eve.holt@reqres.in"); // присваиваем значение полю экземпляра класса email
        body.setPassword("cityslicka");      // присваиваем значение полю экземпляра класса password

        LoginResponseLombokModel response =
                given()
                        .filter(withCustomTemplates()) // добавили расширенный Allure Listener из CustomApiListener
                        .log().all()
                        .contentType(JSON) // обязательно нужно указать тип передаваемых данных
                        .body(body) // в параметр передаем созданный экземпляр класса
                        .when()
                        .post("https://reqres.in/api/login")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        .extract().as(LoginResponseLombokModel.class);

        assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");


    }

    @Test
    void loginWithSpecTest() {

        // создаем тело запроса в виде экземпляра класса (модель Lombok)
        LoginBodyLombokModel body = new LoginBodyLombokModel();  // создаем экземпляр класса
        body.setEmail("eve.holt@reqres.in"); // присваиваем значение полю экземпляра класса email
        body.setPassword("cityslicka");      // присваиваем значение полю экземпляра класса password

        LoginResponseLombokModel response =
                given()
                //given(loginRequestSpec)
                        .spec(loginRequestSpec)
                        .body(body) // в параметр передаем созданный экземпляр класса
                        .when()
                        .post()
                        .then()
                        .spec(loginResponseSpec)
                        .extract().as(LoginResponseLombokModel.class);

        assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");

    }


}
