package ru.netology.domain;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;


import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static void sendRequest(RegistrationData user) {
        // сам запрос
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(new Gson().toJson(user)) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    private static final Faker faker = new Faker(new Locale("en"));

    public static String randomLogin() {
        return faker.name().firstName();
    }

    public static String randomPassword() {
        return faker.internet().password();
    }

    public static class Registration {

        public static RegistrationData getUser(String status) {
                return new RegistrationData(randomLogin(), randomPassword(), status);
        }

        public static RegistrationData getRegisteredUser(String status) {
            RegistrationData registeredUser = getUser(status);
            sendRequest(registeredUser);
            return registeredUser;
        }
    }
}
