package com.github.paulosalonso.research;

import com.github.paulosalonso.research.adapter.controller.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static io.restassured.RestAssured.when;

public class AnswerControllerSecurityIT extends BaseIT {

    @Test
    public void givenAResearchWhenSearchAnswersWithoutAuthenticationThenReturnUnauthorized() {
        when()
                .get("/researches/{researchId}/answers", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void givenAResearchWhenAnsweringItWithoutAuthenticationThenReturnUnauthorized() {
        when()
                .post("/researches/{researchId}/answers", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}
