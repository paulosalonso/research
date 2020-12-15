package com.github.paulosalonso.research.application.controller;

import com.github.paulosalonso.research.application.dto.ResearchInputDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static com.github.paulosalonso.research.application.ResearchHelper.createResearch;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

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
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(id))
                .body("title", equalTo(body.getTitle()))
                .body("description", equalTo(body.getDescription()))
                .body("startsOn", equalTo(ISO_DATE_TIME.format(body.getStartsOn().withOffsetSameInstant(ZoneOffset.UTC))))
                .body("endsOn", equalTo(ISO_DATE_TIME.format(body.getEndsOn().withOffsetSameInstant(ZoneOffset.UTC))));
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
    public void whenSearchWithoutParametersThenReturnAll() {
        truncateDatabase();
        createResearch();
        createResearch();

        given()
                .accept(JSON)
                .when()
                .get("/researches")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(2));
    }

    @Test
    public void whenSearchWithTitleParameterThenReturnFiltered() {
        truncateDatabase();

        createResearch(ResearchInputDTO.builder()
                .title("title-a")
                .description("description-a")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build());

        var researchB = createResearch(ResearchInputDTO.builder()
                .title("title-b")
                .description("description-b")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build());

        given()
                .accept(JSON)
                .queryParam("title", researchB.getTitle())
                .when()
                .get("/researches")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(1))
                .body("id", contains(researchB.getId()));
    }

    @Test
    public void whenSearchWithDescriptionParameterThenReturnFiltered() {
        truncateDatabase();

        createResearch(ResearchInputDTO.builder()
                .title("title-a")
                .description("description-a")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build());

        var researchB = createResearch(ResearchInputDTO.builder()
                .title("title-b")
                .description("description-b")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build());

        given()
                .accept(JSON)
                .queryParam("description", researchB.getDescription())
                .when()
                .get("/researches")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(1))
                .body("id", contains(researchB.getId()));
    }

    @Test
    public void whenSearchWithStartsOnParametersThenReturnFiltered() {
        truncateDatabase();

        createResearch(ResearchInputDTO.builder()
                .title("title-a")
                .description("description-a")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build());

        var researchB = createResearch(ResearchInputDTO.builder()
                .title("title-b")
                .description("description-b")
                .startsOn(OffsetDateTime.now().plusDays(1))
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build());

        var from = ISO_DATE_TIME.format(
                OffsetDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0));

        var to = ISO_DATE_TIME.format(
                OffsetDateTime.now().plusDays(1).withHour(23).withMinute(59).withSecond(59));

        given()
                .accept(JSON)
                .queryParam("startsOnFrom", from)
                .queryParam("startsOnTo", to)
                .when()
                .get("/researches")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(1))
                .body("id", contains(researchB.getId()));
    }

    @Test
    public void whenSearchWithEndsOnParametersThenReturnFiltered() {
        truncateDatabase();

        createResearch(ResearchInputDTO.builder()
                .title("title-a")
                .description("description-a")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build());

        var researchB = createResearch(ResearchInputDTO.builder()
                .title("title-b")
                .description("description-b")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1).plusDays(1))
                .build());

        var from = ISO_DATE_TIME.format(
                OffsetDateTime.now().plusMonths(1).plusDays(1).withHour(0).withMinute(0).withSecond(0));

        var to = ISO_DATE_TIME.format(
                OffsetDateTime.now().plusMonths(1).plusDays(1).withHour(23).withMinute(59).withSecond(59));

        given()
                .accept(JSON)
                .queryParam("endsOnFrom", from)
                .queryParam("endsOnTo", to)
                .when()
                .get("/researches")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(1))
                .body("id", contains(researchB.getId()));
    }

    @Test
    public void whenSearchWithAllParametersThenReturnFiltered() {
        truncateDatabase();

        createResearch(ResearchInputDTO.builder()
                .title("title-a")
                .description("description-a")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build());

        var researchB = createResearch(ResearchInputDTO.builder()
                .title("title-b")
                .description("description-b")
                .startsOn(OffsetDateTime.now().plusDays(1))
                .endsOn(OffsetDateTime.now().plusMonths(1).plusDays(1))
                .build());

        var startsOnFrom = ISO_DATE_TIME.format(
                OffsetDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0));

        var startsOnTo = ISO_DATE_TIME.format(
                OffsetDateTime.now().plusDays(1).withHour(23).withMinute(59).withSecond(59));

        var endsOnFrom = ISO_DATE_TIME.format(
                OffsetDateTime.now().plusMonths(1).plusDays(1).withHour(0).withMinute(0).withSecond(0));

        var endsOnTo = ISO_DATE_TIME.format(
                OffsetDateTime.now().plusMonths(1).plusDays(1).withHour(23).withMinute(59).withSecond(59));

        given()
                .accept(JSON)
                .queryParam("title", researchB.getTitle())
                .queryParam("description", researchB.getDescription())
                .queryParam("startsOnFrom", startsOnFrom)
                .queryParam("startsOnTo", startsOnTo)
                .queryParam("endsOnFrom", endsOnFrom)
                .queryParam("endsOnTo", endsOnTo)
                .when()
                .get("/researches")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(1))
                .body("id", contains(researchB.getId()));
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
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("title", equalTo(body.getTitle()))
                .body("description", equalTo(body.getDescription()))
                .body("startsOn", equalTo(ISO_DATE_TIME.format(body.getStartsOn().withOffsetSameInstant(ZoneOffset.UTC))))
                .body("endsOn", equalTo(ISO_DATE_TIME.format(body.getEndsOn().withOffsetSameInstant(ZoneOffset.UTC))));
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

        var updateBody = ResearchInputDTO.builder()
                .title("updated title")
                .description("updated description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(2))
                .build();

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(updateBody)
                .when()
                .put("/researches/{id}", id)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(id))
                .body("title", equalTo(updateBody.getTitle()))
                .body("description", equalTo(updateBody.getDescription()))
                .body("startsOn", equalTo(ISO_DATE_TIME.format(updateBody.getStartsOn().withOffsetSameInstant(ZoneOffset.UTC))))
                .body("endsOn", equalTo(ISO_DATE_TIME.format(updateBody.getEndsOn().withOffsetSameInstant(ZoneOffset.UTC))));
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
