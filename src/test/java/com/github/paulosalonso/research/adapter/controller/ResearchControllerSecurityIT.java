package com.github.paulosalonso.research.adapter.controller;

import com.github.paulosalonso.research.adapter.controller.dto.ResearchInputDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.equalTo;

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

    @Test
    public void givenAnAdminTokenWithoutTenantClaimWhenAttemptToCreateResearchThenReturnForbidden() {
        var body = ResearchInputDTO.builder()
                .title("title")
                .startsOn(OffsetDateTime.now())
                .build();

        given()
                .contentType(JSON)
                .auth().oauth2(ADMIN_TOKEN_WITHOUT_TENANT_CLAIM)
                .body(body)
                .when()
                .post("/researches")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("status", equalTo(HttpStatus.FORBIDDEN.value()))
                .body("message", equalTo("The JWT does not contain the tenant claim"));
    }

    @Test
    public void givenAnAdminTokenWithoutTenantClaimWhenAttemptToUpdateResearchThenReturnForbidden() {
        var body = ResearchInputDTO.builder()
                .title("title")
                .startsOn(OffsetDateTime.now())
                .build();

        given()
                .contentType(JSON)
                .auth().oauth2(ADMIN_TOKEN_WITHOUT_TENANT_CLAIM)
                .body(body)
                .when()
                .put("/researches/{researchId}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("status", equalTo(HttpStatus.FORBIDDEN.value()))
                .body("message", equalTo("The JWT does not contain the tenant claim"));
    }

    @Test
    public void givenAnAdminTokenWithoutTenantClaimWhenAttemptToDeleteResearchThenReturnForbidden() {
        given()
                .contentType(JSON)
                .auth().oauth2(ADMIN_TOKEN_WITHOUT_TENANT_CLAIM)
                .when()
                .delete("/researches/{researchId}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("status", equalTo(HttpStatus.FORBIDDEN.value()))
                .body("message", equalTo("The JWT does not contain the tenant claim"));
    }
}
