import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class ReqresInTests {

    @CsvSource(value = {
            "0,7,michael.lawson@reqres.in,Michael,Lawson,https://reqres.in/img/faces/7-image.jpg",
            "4,11,george.edwards@reqres.in,George,Edwards,https://reqres.in/img/faces/11-image.jpg"
    })
    @ParameterizedTest
    void checkUserDataTestOnPage(int userIndexOnPage,
                                 int userId,
                                 String email,
                                 String firstName,
                                 String lastName,
                                 String avatarLink) {

        given()
                .log().uri()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data["+userIndexOnPage+"]", hasEntry("id",userId))
                .body("data["+userIndexOnPage+"]", hasEntry("email",email))
                .body("data["+userIndexOnPage+"]", hasEntry("first_name",firstName))
                .body("data["+userIndexOnPage+"]", hasEntry("last_name",lastName))
                .body("data["+userIndexOnPage+"]", hasEntry("avatar",avatarLink));
    }

    @Test
    void checkUserIdOnPage() {

        given()
                .log().uri()
        .when()
                .get("https://reqres.in/api/users?page=2")
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.id",hasItems(7,8,9,10,11,12))
                .assertThat();
    }

    @Test
    void unExistedUrlTest() {

        given()
                .log().uri()
                .when()
                .get("https://reqres.in/api/users/unexistedpage")
                .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }

    @Test
    void createNewUserTest() {

        String newUserData = "{\"name\": \"new_user_name\",\n\"job\": \"qa_engineer\"}";

        given()
                .log().uri()
                .contentType(JSON)
                .body(newUserData)
        .when()
                .post("https://reqres.in/api/users")
        .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("new_user_name"))
                .body("job", is("qa_engineer"));

    }

    @Test
    void updateUserDataTest() {

        String updatedUserData = "{\"name\": \"updated_user_name\",\n\"job\": \"qa_auto_engineer\"}";

        given()
                .log().uri()
                .contentType(JSON)
                .body(updatedUserData)
        .when()
                .patch("https://reqres.in/api/users/2")
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", is("updated_user_name"))
                .body("job", is("qa_auto_engineer"));

    }

    @Test
    void deletePageTest() {

        given()
                .log().uri()
        .when()
                .delete("https://reqres.in/api/users/2")
        .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }

}
