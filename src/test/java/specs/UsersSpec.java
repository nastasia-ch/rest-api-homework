package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.*;
import static io.restassured.filter.log.LogDetail.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.notNullValue;

public class UsersSpec {

    public static RequestSpecification usersRequestSpec = with()
                .baseUri("https://reqres.in")
                .basePath("/api/users")
                .contentType(JSON)
                .log().all();


    public static ResponseSpecification listUsersResponseSpec = new ResponseSpecBuilder()
            .log(STATUS)
            .log(BODY)
            .expectStatusCode(200)
            .expectBody("page", notNullValue())
            .expectBody("per_page", notNullValue())
            .expectBody("total", notNullValue())
            .expectBody("total_pages", notNullValue())
            .expectBody("data", notNullValue())
            .expectBody("support", notNullValue())
            .build();

    public static ResponseSpecification createdUserResponseSpec = new ResponseSpecBuilder()
            .log(STATUS)
            .log(BODY)
            .expectStatusCode(201)
            .expectBody("name",notNullValue())
            .expectBody("job", notNullValue())
            .expectBody("id", notNullValue())
            .expectBody("createdAt", notNullValue())
            .build();

    public static ResponseSpecification updatedUserResponseSpec = new ResponseSpecBuilder()
            .log(STATUS)
            .log(BODY)
            .expectStatusCode(200)
            .expectBody("name",notNullValue())
            .expectBody("job", notNullValue())
            .expectBody("updatedAt", notNullValue())
            .build();

}
