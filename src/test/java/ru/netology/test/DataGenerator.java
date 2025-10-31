package ru.netology.test;

import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Random;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static final Gson gson = new Gson();
    private static final Random random = new Random();

    private DataGenerator() {
    }

    public static void sendRequest(RegistrationDto user) {
        given()
                .spec(requestSpec)
                .body(gson.toJson(user))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static String getRandomLogin() {
        return "user" + System.currentTimeMillis() + random.nextInt(1000);
    }

    public static String getRandomPassword() {
        return "pass" + System.currentTimeMillis() + random.nextInt(1000);
    }

    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationDto getUser(String status) {
            return new RegistrationDto(
                    getRandomLogin(),
                    getRandomPassword(),
                    status
            );
        }

        public static RegistrationDto getRegisteredUser(String status) {
            var registeredUser = getUser(status);
            sendRequest(registeredUser);
            return registeredUser;
        }
    }
}