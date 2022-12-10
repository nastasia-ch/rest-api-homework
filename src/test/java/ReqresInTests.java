import com.github.javafaker.Faker;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Locale;

import static helpers.CustomApiListener.withCustomTemplates;
import static helpers.HelpMethods.getIdOfFirstUserOnPage;
import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.UsersSpec.*;

public class ReqresInTests {

    @CsvSource(value = {
            "1,2,3,emma.wong@reqres.in,Emma,Wong,https://reqres.in/img/faces/3-image.jpg",
            "2,0,7,michael.lawson@reqres.in,Michael,Lawson,https://reqres.in/img/faces/7-image.jpg"
    })
    @Epic("reqres.in")
    @Feature("Работа с данными пользователей")
    @Story("Просмотр данных на странице")
    @Owner("Anastasia Chernega")
    @Severity(SeverityLevel.BLOCKER)
    @Link(value="Link on reqres.in (testing resource)",url="https://reqres.in")
    @DisplayName("Тест: все данные пользователя верно отображены на странице")
    @ParameterizedTest
    void checkUserDataTestOnPage(int pageNumber,
                                 int userIndexOnPage,
                                 int userId,
                                 String email,
                                 String firstName,
                                 String lastName,
                                 String avatarLink) {

        ListUsersResponseModel response =
                given()
                .filter(new AllureRestAssured())
                .spec(usersRequestSpec)
                .queryParam("page", pageNumber)
                .when()
                .get()
                .then()
                .spec(listUsersResponseSpec)
                .extract().as(ListUsersResponseModel.class);

        assertThat(response.getData().get(userIndexOnPage).getId()).isEqualTo(userId);
        assertThat(response.getData().get(userIndexOnPage).getEmail()).isEqualTo(email);
        assertThat(response.getData().get(userIndexOnPage).getFirst_name()).isEqualTo(firstName);
        assertThat(response.getData().get(userIndexOnPage).getLast_name()).isEqualTo(lastName);
        assertThat(response.getData().get(userIndexOnPage).getAvatar()).isEqualTo(avatarLink);
    }

    @CsvSource(value = {
            "1",
            "2"
    })
    @Epic("reqres.in")
    @Feature("Работа с данными пользователей")
    @Story("Просмотр данных на странице")
    @Owner("Anastasia Chernega")
    @Severity(SeverityLevel.BLOCKER)
    @Link(value="Link on reqres.in (testing resource)",url="https://reqres.in")
    @DisplayName("Тест: все пользователи с нужными id отображены на странице")
    @ParameterizedTest
    void checkUsersIdOnPagePositiveTest(int pageNumber) {

        ListUsersResponseModel response =
                given()
                        .filter(new AllureRestAssured())
                        .spec(usersRequestSpec)
                        .queryParam("page", pageNumber)
                        .when()
                        .get()
                        .then()
                        .spec(listUsersResponseSpec)
                        .extract().as(ListUsersResponseModel.class);

        for (int i = 0; i < response.getData().size(); i++) {
            assertThat(response.getData().get(i).getId()).
                    isEqualTo(getIdOfFirstUserOnPage(pageNumber) + i);
        }
    }

        @CsvSource(value = {
               "1,8",
               "2,3"
        })
        @Epic("reqres.in")
        @Feature("Работа с данными пользователей")
        @Story("Просмотр данных на странице")
        @Owner("Anastasia Chernega")
        @Severity(SeverityLevel.BLOCKER)
        @Link(value="Link on reqres.in (testing resource)",url="https://reqres.in")
        @DisplayName("Тест: на странице не отображен пользователь с id с другой страницы")
        @ParameterizedTest
        void checkUserIdOnPageNegativeTest(int pageNumber, int userId) {

            ListUsersResponseModel response =
                    given()
                        .filter(new AllureRestAssured())
                        .spec(usersRequestSpec)
                        .queryParam("page", pageNumber)
                        .when()
                        .get()
                        .then()
                        .spec(listUsersResponseSpec)
                        .extract().as(ListUsersResponseModel.class);

            for (int i = 0; i < response.getData().size(); i++) {
                assertThat(response.getData().get(i).getId()).
                      isNotEqualTo(userId);
            }
        }

    @Epic("reqres.in")
    @Feature("Работа с данными пользователей")
    @Story("Переход на несуществующую страницу")
    @Owner("Anastasia Chernega")
    @Severity(SeverityLevel.BLOCKER)
    @Link(value="Link on reqres.in (testing resource)",url="https://reqres.in")
    @DisplayName("Тест: при переходе на несуществующую страницу код ответа 404")
    void unExistedUrlTest() {

        given()
                .filter(new AllureRestAssured())
                .spec(usersRequestSpec)
                .when()
                .get("/unexistedpage")
                .then()
                .log().all()
                .statusCode(404);
    }

    @Epic("reqres.in")
    @Feature("Работа с данными пользователей")
    @Story("Создание нового пользователя")
    @Owner("Anastasia Chernega")
    @Severity(SeverityLevel.BLOCKER)
    @Link(value="Link on reqres.in (testing resource)",url="https://reqres.in")
    @DisplayName("Тест: создание нового пользователя")
    @Test
    void createNewUserTest() {

        Faker faker = new Faker(new Locale("en"));
        String firstName = faker.name().firstName();
        String jobPosition = faker.job().position();

        NewUserRequestModel request = new NewUserRequestModel();
        request.setName(firstName);
        request.setJob(jobPosition);

        NewUserResponseModel response =
                 given()
                .filter(withCustomTemplates())
                .spec(usersRequestSpec)
                .body(request)
                .when()
                .post()
                .then()
                .spec(createdUserResponseSpec)
                .extract().as(NewUserResponseModel.class);

        assertThat(response.getName()).isEqualTo(firstName);
        assertThat(response.getJob()).isEqualTo(jobPosition);

    }

    @Epic("reqres.in")
    @Feature("Работа с данными пользователей")
    @Story("Редактирование данных пользователя")
    @Owner("Anastasia Chernega")
    @Severity(SeverityLevel.BLOCKER)
    @Link(value="Link on reqres.in (testing resource)",url="https://reqres.in")
    @DisplayName("Тест: обновление данных о пользователе")
    @Test
    void updateUserDataTest() {

        Faker faker = new Faker(new Locale("en"));
        String firstName = faker.name().firstName();
        String jobPosition = faker.job().position();

        UpdateUserRequestModel request = new UpdateUserRequestModel();
        request.setName(firstName);
        request.setJob(jobPosition);

        UpdateUserResponseModel response =
                 given()
                .filter(withCustomTemplates())
                .spec(usersRequestSpec)
                .body(request)
                .when()
                .patch("/2")
                .then()
                .spec(updatedUserResponseSpec)
                .extract().as(UpdateUserResponseModel.class);

        assertThat(response.getName()).isEqualTo(firstName);
        assertThat(response.getJob()).isEqualTo(jobPosition);

    }

    @Epic("reqres.in")
    @Feature("Работа с данными пользователей")
    @Story("Удаление данных пользователя")
    @Owner("Anastasia Chernega")
    @Severity(SeverityLevel.BLOCKER)
    @Link(value="Link on reqres.in (testing resource)",url="https://reqres.in")
    @DisplayName("Тест: удаление данных о пользователе")
    @Test
    void deletePageTest() {

        given()
                .filter(withCustomTemplates())
                .spec(usersRequestSpec)
                .when()
                .delete("/2")
                .then()
                .log().all()
                .statusCode(204);
    }

}
