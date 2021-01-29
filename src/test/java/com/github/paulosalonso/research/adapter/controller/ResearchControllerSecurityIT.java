package com.github.paulosalonso.research.adapter.controller;

import com.github.paulosalonso.research.adapter.controller.dto.ResearchInputDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;

public class ResearchControllerSecurityIT extends BaseIT {

    @Test
    public void whenAttemptToGetResearchWithoutAuthorizationThenReturnUnauthorized() {
        when()
                .get("/researches/{researchId}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void whenAttemptToSearchResearchWithoutAuthorizationThenReturnUnauthorized() {
        when()
                .get("/researches")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void whenAttemptToCreateResearchWithoutAuthorizationThenReturnUnauthorized() {
        when()
                .post("/researches")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void whenAttemptToUpdateResearchWithoutAuthorizationThenReturnUnauthorized() {
        when()
                .put("/researches/{researchId}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void whenAttemptToDeleteResearchWithoutAuthorizationThenReturnUnauthorized() {
        when()
                .delete("/researches/{researchId}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void givenANormalUserTokenWhenAttemptToCreateResearchThenReturnForbidden() {
        var body = ResearchInputDTO.builder()
                .title("title")
                .startsOn(OffsetDateTime.now())
                .build();

        givenAuthenticatedUser()
                .contentType(JSON)
                .body(body)
                .when()
                .post("/researches")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void givenANormalUserTokenWhenAttemptToUpdateResearchThenReturnForbidden() {
        var body = ResearchInputDTO.builder()
                .title("title")
                .startsOn(OffsetDateTime.now())
                .build();

        givenAuthenticatedUser()
                .contentType(JSON)
                .body(body)
                .when()
                .put("/researches/{researchId}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void givenANormalUserTokenWhenAttemptToDeleteResearchThenReturnForbidden() {
        givenAuthenticatedUser()
                .when()
                .delete("/researches/{researchId}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
}
