package com.github.paulosalonso.research.application.controller;

import com.github.paulosalonso.research.application.dto.ResearchInputDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;

public class ResearchControllerIT extends BaseIT {

    @Test
    public void whenGetThenReturnOk() {
        var body = ResearchInputDTO.builder()
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build();

        String id = given()
                .contentType(JSON)
                .accept(JSON)
                .body(body)
                .when()
                .post("/researches")
                .path("id");

        given()
                .accept(JSON)
                .when()
                .get("/researches/{id}", id)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void givenANonexistentIdWhenGetThenReturnNotFound() {
        given()
                .accept(JSON)
                .when()
                .get("/researches/{id}", UUID.randomUUID().toString())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void givenAnInvalidUUIdWhenGetThenReturnBadRequest() {
        given()
                .accept(JSON)
                .when()
                .get("/researches/{id}", "invalid-uuid")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void whenSearchThenReturnOk() {
        given()
                .accept(JSON)
                .when()
                .get("/researches")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void whenCreateThenReturnCreated() {
        var body = ResearchInputDTO.builder()
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build();

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(body)
                .when()
                .post("/researches")
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void whenCreateWithNullRequiredValueThenReturnBadRequest() {
        given()
                .contentType(JSON)
                .accept(JSON)
                .body(ResearchInputDTO.builder().build())
                .when()
                .post("/researches")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void whenUpdateThenReturnOk() {
        var body = ResearchInputDTO.builder()
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build();

        String id = given()
                .contentType(JSON)
                .accept(JSON)
                .body(body)
                .when()
                .post("/researches")
                .path("id");

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(body)
                .when()
                .put("/researches/{id}", id)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void givenANonexistentIdWhenUpdateThenReturnNotFound() {
        var body = ResearchInputDTO.builder()
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build();

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(body)
                .when()
                .put("/researches/{id}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void whenUpdateWithNullRequiredValueThenReturnBadRequest() {
        given()
                .contentType(JSON)
                .accept(JSON)
                .body(ResearchInputDTO.builder().build())
                .when()
                .put("/researches/{id}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void whenUpdateWithInvalidUUIDThenReturnBadRequest() {
        var body = ResearchInputDTO.builder()
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build();

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(body)
                .when()
                .put("/researches/{id}", "invalid-uuid")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void whenDeleteThenReturnNoContent() {
        var body = ResearchInputDTO.builder()
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build();

        String id = given()
                .contentType(JSON)
                .accept(JSON)
                .body(body)
                .when()
                .post("/researches")
                .path("id");

        when().delete("/researches/{id}", id)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void givenANonexistentIdWhenDeleteThenReturnNotFound() {
        when().delete("/researches/{id}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void givenAnInvalidUUIDWhenDeleteThenReturnBadRequest() {
        when().delete("/researches/{id}", "invalid-uuid")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
