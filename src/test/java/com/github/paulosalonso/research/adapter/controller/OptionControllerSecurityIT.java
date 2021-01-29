package com.github.paulosalonso.research.adapter.controller;

import com.github.paulosalonso.research.adapter.controller.dto.OptionInputDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;

public class OptionControllerSecurityIT extends BaseIT {

    @Test
    public void whenAttemptToGetOptionWithoutAuthorizationThenReturnUnauthorized() {
        when()
                .get("/questions/{questionId}/options/{optionId}", UUID.randomUUID(), UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void whenAttemptToSearchOptionsWithoutAuthorizationThenReturnUnauthorized() {
        when()
                .get("/questions/{questionId}/options", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void whenAttemptToCreateOptionWithoutAuthorizationThenReturnUnauthorized() {
        when()
                .post("/questions/{questionId}/options", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void whenAttemptToUpdateOptionWithoutAuthorizationThenReturnUnauthorized() {
        when()
                .put("/questions/{questionId}/options/{optionId}", UUID.randomUUID(), UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void whenAttemptToDeleteOptionWithoutAuthorizationThenReturnUnauthorized() {
        when()
                .delete("/questions/{questionId}/options/{optionId}", UUID.randomUUID(), UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void givenANormalUserTokenWhenAttemptToCreateOptionThenReturnForbidden() {
        var body = OptionInputDTO.builder()
                .description("description")
                .notify(false)
                .build();

        givenAuthenticatedUser()
                .contentType(JSON)
                .body(body)
                .when()
                .post("/questions/{questionId}/options", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void givenANormalUserTokenWhenAttemptToUpdateOptionThenReturnForbidden() {
        var body = OptionInputDTO.builder()
                .description("description")
                .notify(false)
                .build();

        givenAuthenticatedUser()
                .contentType(JSON)
                .body(body)
                .when()
                .put("/questions/{questionId}/options/{optionId}", UUID.randomUUID(), UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void givenANormalUserTokenWhenAttemptToDeleteOptionThenReturnForbidden() {
        givenAuthenticatedUser()
                .when()
                .delete("/questions/{questionId}/options/{optionId}", UUID.randomUUID(), UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
}
