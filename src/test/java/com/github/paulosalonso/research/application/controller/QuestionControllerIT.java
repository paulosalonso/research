package com.github.paulosalonso.research.application.controller;

import com.github.paulosalonso.research.application.dto.QuestionInputDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.github.paulosalonso.research.application.QuestionCreator.createQuestion;
import static com.github.paulosalonso.research.application.ResearchCreator.createResearch;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

public class QuestionControllerIT extends BaseIT {

    @Test
    public void whenGetThenReturnOk() {
        var research = createResearch();

        var body = QuestionInputDTO.builder()
                .description("description")
                .multiSelect(false)
                .build();

        String questionId = given()
                .contentType(JSON)
                .accept(JSON)
                .body(body)
                .post("/researches/{researchId}/questions", research.getId())
                .path("id");

        given()
                .accept(JSON)
                .when()
                .get("/researches/{researchId}/questions/{questionId}", research.getId(), questionId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(questionId))
                .body("description", equalTo(body.getDescription()))
                .body("multiSelect", equalTo(body.getMultiSelect()));
    }

    @Test
    public void givenANonexistentResearchIdWhenGetThenReturnNotFound() {
        var research = createResearch();
        var question = createQuestion(UUID.fromString(research.getId()));

        given()
                .accept(JSON)
                .when()
                .get("/researches/{researchId}/questions/{questionId}", UUID.randomUUID(), question.getId())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void givenANonexistentQuestionIdWhenGetThenReturnNotFound() {
        var research = createResearch();

        given()
                .accept(JSON)
                .when()
                .get("/researches/{researchId}/questions/{questionId}", research.getId(), UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void whenSearchWithoutParametersThenReturnAll() {
        truncateDatabase();

        var researchId = UUID.fromString(createResearch().getId());

        createQuestion(researchId);
        createQuestion(researchId);

        given()
                .accept(JSON)
                .when()
                .get("/researches/{researchId}/questions", researchId.toString())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(2));
    }

    @Test
    public void whenSearchWithDescriptionParameterThenReturnFiltered() {
        truncateDatabase();

        var research = createResearch();

        createQuestion(UUID.fromString(research.getId()), QuestionInputDTO.builder()
                .description("description-a")
                .multiSelect(true)
                .build());

        var questionB = createQuestion(UUID.fromString(research.getId()), QuestionInputDTO.builder()
                .description("description-b")
                .multiSelect(true)
                .build());

        given()
                .accept(JSON)
                .queryParam("description", questionB.getDescription())
                .when()
                .get("/researches/{researchId}/questions", research.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(1))
                .body("id", contains(questionB.getId()));
    }

    @Test
    public void whenSearchWithMultiSelectParameterThenReturnFiltered() {
        truncateDatabase();

        var research = createResearch();

        createQuestion(UUID.fromString(research.getId()), QuestionInputDTO.builder()
                .description("description-a")
                .multiSelect(false)
                .build());

        var questionB = createQuestion(UUID.fromString(research.getId()), QuestionInputDTO.builder()
                .description("description-b")
                .multiSelect(true)
                .build());

        given()
                .accept(JSON)
                .queryParam("multiSelect", questionB.getMultiSelect())
                .when()
                .get("/researches/{researchId}/questions", research.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(1))
                .body("id", contains(questionB.getId()));
    }

    @Test
    public void whenSearchWithAllParametersThenReturnFiltered() {
        truncateDatabase();

        var research = createResearch();

        createQuestion(UUID.fromString(research.getId()), QuestionInputDTO.builder()
                .description("description-a")
                .multiSelect(false)
                .build());

        var questionB = createQuestion(UUID.fromString(research.getId()), QuestionInputDTO.builder()
                .description("description-b")
                .multiSelect(true)
                .build());

        given()
                .accept(JSON)
                .queryParam("description", questionB.getDescription())
                .queryParam("multiSelect", questionB.getMultiSelect())
                .when()
                .get("/researches/{researchId}/questions", research.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(1))
                .body("id", contains(questionB.getId()));
    }

    @Test
    public void whenCreateThenReturnCreated() {
        var research = createResearch();

        var body = QuestionInputDTO.builder()
                .description("description")
                .multiSelect(true)
                .build();

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(body)
                .when()
                .post("/researches/{researchId}/questions", research.getId())
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("description", equalTo(body.getDescription()))
                .body("multiSelect", equalTo(body.getMultiSelect()));
    }

    @Test
    public void whenCreateWithNonexistentResearchIdThenReturnNotFound() {
        var body = QuestionInputDTO.builder()
                .description("description")
                .multiSelect(false)
                .build();

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(body)
                .when()
                .post("/researches/{researchId}/questions", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void whenCreateWithNullRequiredValueThenReturnBadRequest() {
        var research = createResearch();

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(QuestionInputDTO.builder().build())
                .when()
                .post("/researches/{researchId}/questions", research.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void whenUpdateThenReturnOk() {
        var research = createResearch();
        var question = createQuestion(UUID.fromString(research.getId()));

        var updateBody = QuestionInputDTO.builder()
                .description(question.getDescription() + " updated")
                .multiSelect(!question.getMultiSelect())
                .build();

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(updateBody)
                .when()
                .put("researches/{researchId}/questions/{questionId}", research.getId(), question.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(question.getId()))
                .body("description", equalTo(updateBody.getDescription()))
                .body("multiSelect", equalTo(updateBody.getMultiSelect()));
    }

    @Test
    public void givenANonexistentResearchIdWhenUpdateThenReturnNotFound() {
        var research = createResearch();
        var question = createQuestion(UUID.fromString(research.getId()));

        var updateBody = QuestionInputDTO.builder()
                .description(question.getDescription() + " updated")
                .multiSelect(!question.getMultiSelect())
                .build();

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(updateBody)
                .when()
                .put("researches/{researchId}/questions/{questionId}", UUID.randomUUID(), question.getId())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void givenANonexistentQuestionIdWhenUpdateThenReturnNotFound() {
        var research = createResearch();
        var question = createQuestion(UUID.fromString(research.getId()));

        var updateBody = QuestionInputDTO.builder()
                .description(question.getDescription() + " updated")
                .multiSelect(!question.getMultiSelect())
                .build();

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(updateBody)
                .when()
                .put("researches/{researchId}/questions/{questionId}", research.getId(), UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void whenUpdateWithNullRequiredValueThenReturnBadRequest() {
        var research = createResearch();
        var question = createQuestion(UUID.fromString(research.getId()));

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(QuestionInputDTO.builder().build())
                .when()
                .put("researches/{researchId}/questions/{questionId}", research.getId(), question.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void givenAnInvalidResearchUUIDWhenUpdateThenReturnBadRequest() {
        given()
                .contentType(JSON)
                .accept(JSON)
                .body(QuestionInputDTO.builder().build())
                .when()
                .put("researches/{researchId}/questions/{questionId}", "invalid-uuid", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void givenAnInvalidQuestionUUIDWhenUpdateThenReturnBadRequest() {
        given()
                .contentType(JSON)
                .accept(JSON)
                .body(QuestionInputDTO.builder().build())
                .when()
                .put("researches/{researchId}/questions/{questionId}", UUID.randomUUID(), "invalid-uuid")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void whenDeleteThenReturnNoContent() {
        var research = createResearch();
        var question = createQuestion(UUID.fromString(research.getId()));

        given()
                .contentType(JSON)
                .accept(JSON)
                .when()
                .delete("/researches/{researchId}/questions/{questionId}", research.getId(), question.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void givenANonexistentResearchIdWhenDeleteThenReturnNotFound() {
        var research = createResearch();
        var question = createQuestion(UUID.fromString(research.getId()));

        given()
                .contentType(JSON)
                .accept(JSON)
                .when()
                .delete("/researches/{researchId}/questions/{questionId}", UUID.randomUUID(), question.getId())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void givenANonexistentQuestionIdWhenDeleteThenReturnNotFound() {
        var research = createResearch();

        given()
                .contentType(JSON)
                .accept(JSON)
                .when()
                .delete("/researches/{researchId}/questions/{questionId}", research.getId(), UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void givenAnInvalidResearchUUIDWhenDeleteThenReturnBadRequest() {
        given()
                .contentType(JSON)
                .accept(JSON)
                .when()
                .delete("/researches/{researchId}/questions/{questionId}", "invalid-uuid", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void givenAnInvalidQuestionUUIDWhenDeleteThenReturnBadRequest() {
        given()
                .contentType(JSON)
                .accept(JSON)
                .when()
                .delete("/researches/{researchId}/questions/{questionId}", UUID.randomUUID(), "invalid-uuid")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
