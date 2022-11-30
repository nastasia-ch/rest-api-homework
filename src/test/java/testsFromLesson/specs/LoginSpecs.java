package testsFromLesson.specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.notNullValue;
import static testsFromLesson.helpers.CustomApiListener.withCustomTemplates;

public class LoginSpecs {

    public static RequestSpecification loginRequestSpec = with()
            .filter(withCustomTemplates())
            .baseUri("https://reqres.in")
            .basePath("api/login")
            .log().all()
            .contentType(JSON);

    public static ResponseSpecification loginResponseSpec = new ResponseSpecBuilder()
            .log(STATUS)
            .log(BODY)
            .expectStatusCode(200)
            .expectBody("token", notNullValue())
            .build()
}
