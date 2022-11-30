package testsFromLesson.helpers;

import io.qameta.allure.restassured.AllureRestAssured;

public class CustomApiListener {

    private static final AllureRestAssured FILTER = new AllureRestAssured();
    //создали AllureRestAssured фильтр (тот же, что добавили в тело запроса и расширили его ->

    public static AllureRestAssured withCustomTemplates() {
        FILTER.setRequestTemplate("request.ftl"); // добавили шаблон для запроса из файла request.ftl
        FILTER.setResponseTemplate("response.ftl"); // добавили шаблон для ответа из файла response.ftl
        return FILTER;
    }

}
