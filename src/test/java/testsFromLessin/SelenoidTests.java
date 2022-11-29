package testsFromLessin;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SelenoidTests {

    /*
    1. Сделать запрос на https://selenoid.autotests.cloud/status
    2. Получить ответ {   "total": 20,   "used": 0,   "queued": 0,   "pending": 0,
    "browsers": {     "android": {       "8.1": {}     },     "chrome": {       "100.0": {},       "99.0": {}     },
    "chrome-mobile": {       "86.0": {}     },     "firefox": {       "97.0": {},       "98.0": {}     },
    "opera": {       "84.0": {},       "85.0": {}     }   } }
    3. Проверить total равно 20
    */

    @Test
    void checkTotal() {

        when()
            .get("https://selenoid.autotests.cloud/status")
        .then()
             .statusCode(200)
             .body("total", is(20));

    }

    @Test
    void checkTotalWithLogs() {

        given().
            log().all(). // логирование в терминале запроса
        when().
            get("https://selenoid.autotests.cloud/status").
        then().
            log().all(). // логирование в терминале ответа на запрос
            statusCode(200).
            body("total", is(20));

    }

    @Test
    void checkTotalWithSomeLogs() {  // выборочно указываем, какие логи запроса и ответа нам нужны в терминале

        given().
                log().uri(). // логирование в терминале только url, на который отправлем запрос
                when().
                get("https://selenoid.autotests.cloud/status").
                then().
                log().status(). // логирование в терминале статуса ответа на запрос
                log().body(). // логирование в терминале тела ответа
                statusCode(200).
                body("total", is(20));

    }

    @Test
    void checkChromeVersion() {

        given().
                log().uri().
                when().
                get("https://selenoid.autotests.cloud/status").
                then().
                log().status().
                log().body().
                statusCode(200).
                body("browsers.chrome", hasKey("100.0"));

    }

    @Test
    void checkResponseBadPractice() {
        String expectedResults = "{   \"total\": 20,   \"used\": 0,   \"queued\": 0,   \"pending\": 0,   \"browsers\": {     \"android\": {       \"8.1\": {}     },     \"chrome\": {       \"100.0\": {},       \"99.0\": {}     },     \"chrome-mobile\": {       \"86.0\": {}     },     \"firefox\": {       \"97.0\": {},       \"98.0\": {}     },     \"opera\": {       \"84.0\": {},       \"85.0\": {}     }   } }";
        Response actualResponse = given()
                .log().uri()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().response();

        assertEquals(expectedResults,actualResponse.toString());
    }

    @Test
    void checkResponseGoodPractice() {
        Integer expectedTotal = 20;
        Integer actualTotal = given()
                .log().uri()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().path("total");

        assertEquals(expectedTotal,actualTotal);
    }

     /*
    1. Сделать запрос на https://selenoid.autotests.cloud/wd/hub/status
    2. Получить ответ {   "value": {     "message": "Selenoid 1.10.7 built at 2021-11-21_05:46:32AM",     "ready": true   } }
    3. Проверить value.raady is true
    */

    @Test
    void checkGWdHubStatus401() {
        given()
                .log().uri()
        .when()
                .get("https://selenoid.autotests.cloud/wd/hub/status")
        .then()
                .log().status()
                .log().body()
                .statusCode(401);

    }

    @Test
    void checkGWdHubStatus200() {
        given()
                .log().uri()
        .when()
                .get("https://user1:1234@selenoid.autotests.cloud/wd/hub/status")
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("value.ready", is(true));

    }

    @Test
    void checkGWdHubWithAuthStatus() {
        given()
                .log().uri()
                .auth().basic("user1","1234")
        .when()
                .get("https://selenoid.autotests.cloud/wd/hub/status")
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("value.ready", is(true));

    }

}
