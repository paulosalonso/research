package com.github.paulosalonso.research.adapter.controller;

import com.github.paulosalonso.research.adapter.controller.dto.QuestionInputDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;

public class QuestionControllerSecurityIT extends BaseIT {

    @Test
    public void whenAttemptToGetQuestionWithoutAuthorizationThenReturnUnauthorized() {
        when()
                .get("/researches/{researchId}/questions/{questionId}", UUID.randomUUID(), UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void whenAttemptToSearchQuestionsWithoutAuthorizationThenReturnUnauthorized() {
        when()
                .get("/researches/{researchId}/questions", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void whenAttemptToCreateQuestionWithoutAuthorizationThenReturnUnauthorized() {
        when()
                .post("/researches/{researchId}/questions", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void whenAttemptToUpdateQuestionWithoutAuthorizationThenReturnUnauthorized() {
        when()
                .put("/researches/{researchId}/questions/{questionId}", UUID.randomUUID(), UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void whenAttemptToDeleteQuestionWithoutAuthorizationThenReturnUnauthorized() {
        when()
                .delete("/researches/{researchId}/questions/{questionId}", UUID.randomUUID(), UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void givenANormalUserTokenWhenAttemptToCreateQuestionThenReturnForbidden() {
        var body = QuestionInputDTO.builder()
                .description("description")
                .multiSelect(false)
                .build();

        givenAuthenticatedUser()
                .contentType(JSON)
                .body(body)
                .when()
                .post("/researches/{researchId}/questions", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void givenANormalUserTokenWhenAttemptToUpdateQuestionThenReturnForbidden() {
        var body = QuestionInputDTO.builder()
                .description("description")
                .multiSelect(false)
                .build();

        givenAuthenticatedUser()
                .contentType(JSON)
                .body(body)
                .when()
                .put("/researches/{researchId}/questions/{questionId}", UUID.randomUUID(), UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void givenANormalUserTokenWhenAttemptToDeleteQuestionThenReturnForbidden() {
        givenAuthenticatedUser()
                .when()
                .delete("/researches/{researchId}/questions/{questionId}", UUID.randomUUID(), UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
}
