import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static specs.UsersSpec.*;

public class ReqresInTestsWithGroovy {

    static Stream<Arguments> checkUsersIdOnPageTestWithGroovy() {
        return Stream.of(
                Arguments.of(1, List.of(1,2,3,4,5,6)),
                Arguments.of(2, List.of(7,8,9,10,11,12))
        );
    }
    @MethodSource
    @ParameterizedTest
    void checkUsersIdOnPageTestWithGroovy(int pageNumber, List<Integer> usersId) {

        List<Integer> response =
                given()
                        .filter(new AllureRestAssured())
                        .spec(usersRequestSpec)
                        .queryParam("page", pageNumber)
                        .when()
                        .get()
                        .then()
                        .spec(listUsersResponseSpec)
                        .extract()
                        .path("data.findAll{it.id}.id.flatten()");

        for(int id: usersId) {
            assertThat(response).contains(id);
        }

    }

    @CsvSource(value = {
            "1, eve.holt@reqres.in",
            "2, byron.fields@reqres.in"
    })
    @ParameterizedTest
    void checkUserEmailOnPageTestWithGroovy(int pageNumber, String expectedEmail) {

                given()
                        .filter(new AllureRestAssured())
                        .spec(usersRequestSpec)
                        .queryParam("page", pageNumber)
                        .when()
                        .get()
                        .then()
                        .spec(listUsersResponseSpec)
                        .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                                hasItem(expectedEmail));

    }

}


