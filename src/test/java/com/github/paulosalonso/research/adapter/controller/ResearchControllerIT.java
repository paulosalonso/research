package com.github.paulosalonso.research.adapter.controller;

import com.github.paulosalonso.research.adapter.controller.dto.ResearchInputDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static com.github.paulosalonso.research.adapter.controller.OptionCreator.createOption;
import static com.github.paulosalonso.research.adapter.controller.QuestionCreator.createQuestion;
import static com.github.paulosalonso.research.adapter.controller.ResearchCreator.createResearch;
import static io.restassured.http.ContentType.JSON;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.*;

public class ResearchControllerIT extends BaseIT {

    @Test
    public void whenGetWithoutQuestionsThenReturnOk() {
        var body = ResearchInputDTO.builder()
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build();

        String id = givenAuthenticatedAdmin()
                .contentType(JSON)
                .accept(JSON)
                .body(body)
                .post("/researches")
                .path("id");

        givenAuthenticatedUser()
                .accept(JSON)
                .when()
                .get("/researches/{id}", id)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(id))
                .body("title", equalTo(body.getTitle()))
                .body("description", equalTo(body.getDescription()))
                .body("startsOn", equalTo(ISO_DATE_TIME.format(body.getStartsOn().withOffsetSameInstant(ZoneOffset.UTC))))
                .body("endsOn", equalTo(ISO_DATE_TIME.format(body.getEndsOn().withOffsetSameInstant(ZoneOffset.UTC))))
                .body("$", not(hasKey("questions")));
    }

    @Test
    public void whenGetWithQuestionsThenReturnOk() {
        var body = ResearchInputDTO.builder()
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build();

        String id = givenAuthenticatedAdmin()
                .contentType(JSON)
                .accept(JSON)
                .body(body)
                .when()
                .post("/researches")
                .path("id");

        var question = createQuestion(UUID.fromString(id));
        var option = createOption(question.getId());

        givenAuthenticatedAdmin()
                .accept(JSON)
                .queryParam("fillQuestions", true)
                .when()
                .get("/researches/{id}", id)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(id))
                .body("title", equalTo(body.getTitle()))
                .body("description", equalTo(body.getDescription()))
                .body("startsOn", equalTo(ISO_DATE_TIME.format(body.getStartsOn().withOffsetSameInstant(ZoneOffset.UTC))))
                .body("endsOn", equalTo(ISO_DATE_TIME.format(body.getEndsOn().withOffsetSameInstant(ZoneOffset.UTC))))
                .body("questions", hasSize(1))
                .body("questions.id", contains(question.getId().toString()))
                .body("questions.options", hasSize(1))
                .body("questions[0].options.id", contains(option.getId().toString()));
    }

    @Test
    public void givenANonexistentIdWhenGetThenReturnNotFound() {
        givenAuthenticatedAdmin()
                .accept(JSON)
                .when()
                .get("/researches/{id}", UUID.randomUUID().toString())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", equalTo(HttpStatus.NOT_FOUND.value()))
                .body("message", equalTo("Requested resource not found"))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void givenAnInvalidUUIdWhenGetThenReturnBadRequest() {
        givenAuthenticatedAdmin()
                .accept(JSON)
                .auth().oauth2(ADMIN_TOKEN)
                .when()
                .get("/researches/{id}", "invalid-uuid")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("'invalid-uuid' is an invalid value for the 'id' URL parameter. Required type is 'UUID'."))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void whenSearchWithoutParametersThenReturnAll() {
        truncateDatabase();

        var researchA = createResearch();
        var researchB = createResearch();

        givenAuthenticatedAdmin()
                .accept(JSON)
                .when()
                .get("/researches")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(2))
                .body("id", hasItems(researchA.getId().toString(), researchB.getId().toString()));
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

        givenAuthenticatedAdmin()
                .accept(JSON)
                .queryParam("title", researchB.getTitle())
                .when()
                .get("/researches")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(1))
                .body("id", contains(researchB.getId().toString()));
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

        givenAuthenticatedAdmin()
                .accept(JSON)
                .queryParam("description", researchB.getDescription())
                .when()
                .get("/researches")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(1))
                .body("id", contains(researchB.getId().toString()));
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

        givenAuthenticatedAdmin()
                .accept(JSON)
                .queryParam("startsOnFrom", from)
                .queryParam("startsOnTo", to)
                .when()
                .get("/researches")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(1))
                .body("id", contains(researchB.getId().toString()));
    }

    @Test
    public void whenSearchWithStartsOnParametersWithPreviousPeriodThenReturnEmpty() {
        truncateDatabase();

        createResearch(ResearchInputDTO.builder()
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build());

        var to = ISO_DATE_TIME.format(
                OffsetDateTime.now().minusDays(1).withHour(23).withMinute(59).withSecond(59));

        givenAuthenticatedAdmin()
                .accept(JSON)
                .queryParam("startsOnTo", to)
                .when()
                .get("/researches")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", empty());
    }

    @Test
    public void whenSearchWithStartsOnParametersWithLaterPeriodThenReturnEmpty() {
        truncateDatabase();

        createResearch(ResearchInputDTO.builder()
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build());

        var from = ISO_DATE_TIME.format(
                OffsetDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0));

        givenAuthenticatedAdmin()
                .accept(JSON)
                .queryParam("startsOnFrom", from)
                .when()
                .get("/researches")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", empty());
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

        givenAuthenticatedAdmin()
                .accept(JSON)
                .queryParam("endsOnFrom", from)
                .queryParam("endsOnTo", to)
                .when()
                .get("/researches")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(1))
                .body("id", contains(researchB.getId().toString()));
    }

    @Test
    public void whenSearchWithEndsOnParametersWithPreviousPeriodThenReturnEmpty() {
        truncateDatabase();

        createResearch(ResearchInputDTO.builder()
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build());

        var to = ISO_DATE_TIME.format(
                OffsetDateTime.now().plusMonths(1).minusDays(1).withHour(23).withMinute(59).withSecond(59));

        givenAuthenticatedAdmin()
                .accept(JSON)
                .queryParam("endsOnTo", to)
                .when()
                .get("/researches")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", empty());
    }

    @Test
    public void whenSearchWithEndsOnParametersWithLaterPeriodThenReturnEmpty() {
        truncateDatabase();

        createResearch(ResearchInputDTO.builder()
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build());

        var from = ISO_DATE_TIME.format(
                OffsetDateTime.now().plusMonths(1).plusDays(1).withHour(0).withMinute(0).withSecond(0));

        givenAuthenticatedAdmin()
                .accept(JSON)
                .queryParam("endsOnFrom", from)
                .when()
                .get("/researches")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", empty());
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

        givenAuthenticatedAdmin()
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
                .body("id", contains(researchB.getId().toString()));
    }

    @Test
    public void whenCreateThenReturnCreated() {
        var body = ResearchInputDTO.builder()
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build();

        givenAuthenticatedAdmin()
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
        givenAuthenticatedAdmin()
                .contentType(JSON)
                .accept(JSON)
                .header("Accept-Language", "en-US")
                .body(ResearchInputDTO.builder().build())
                .when()
                .post("/researches")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo("Invalid field(s)"))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("fields", hasSize(2))
                .body("fields.name", hasItems("title", "startsOn"))
                .body("fields.message", hasItems("must not be blank", "must not be null"));
    }

    @Test
    public void whenUpdateThenReturnOk() {
        var body = ResearchInputDTO.builder()
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build();

        String id = givenAuthenticatedAdmin()
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

        givenAuthenticatedAdmin()
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

        givenAuthenticatedAdmin()
                .contentType(JSON)
                .accept(JSON)
                .body(body)
                .when()
                .put("/researches/{id}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", equalTo(HttpStatus.NOT_FOUND.value()))
                .body("message", equalTo("Requested resource not found"))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void whenUpdateWithNullRequiredValueThenReturnBadRequest() {
        givenAuthenticatedAdmin()
                .contentType(JSON)
                .accept(JSON)
                .header("Accept-Language", "en-US")
                .body(ResearchInputDTO.builder().build())
                .when()
                .put("/researches/{id}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo("Invalid field(s)"))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("fields", hasSize(2))
                .body("fields.name", hasItems("title", "startsOn"))
                .body("fields.message", hasItems("must not be blank", "must not be null"));
    }

    @Test
    public void whenUpdateWithInvalidUUIDThenReturnBadRequest() {
        var body = ResearchInputDTO.builder()
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build();

        givenAuthenticatedAdmin()
                .contentType(JSON)
                .accept(JSON)
                .body(body)
                .when()
                .put("/researches/{id}", "invalid-uuid")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("'invalid-uuid' is an invalid value for the 'id' URL parameter. Required type is 'UUID'."))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void whenDeleteThenReturnNoContent() {
        var body = ResearchInputDTO.builder()
                .title("title")
                .description("description")
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build();

        String id = givenAuthenticatedAdmin()
                .contentType(JSON)
                .accept(JSON)
                .body(body)
                .post("/researches")
                .path("id");

        givenAuthenticatedAdmin()
                .when().delete("/researches/{id}", id)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void givenANonexistentIdWhenDeleteThenReturnNotFound() {
        givenAuthenticatedAdmin()
                .when().delete("/researches/{id}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", equalTo(HttpStatus.NOT_FOUND.value()))
                .body("message", equalTo("Requested resource not found"))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void givenAnInvalidUUIDWhenDeleteThenReturnBadRequest() {
        givenAuthenticatedAdmin()
                .when().delete("/researches/{id}", "invalid-uuid")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo("'invalid-uuid' is an invalid value for the 'id' URL parameter. Required type is 'UUID'."))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }
}
