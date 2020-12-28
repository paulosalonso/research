package com.github.paulosalonso.research.application.controller;

import com.github.paulosalonso.research.application.dto.ResearchAnswerInputDTO;
import com.github.paulosalonso.research.application.dto.ResearchAnswerInputDTO.QuestionAnswerInputDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import static com.github.paulosalonso.research.application.AnswerCreator.createAnswer;
import static com.github.paulosalonso.research.application.OptionCreator.createOption;
import static com.github.paulosalonso.research.application.QuestionCreator.createQuestion;
import static com.github.paulosalonso.research.application.ResearchCreator.createResearch;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;

public class AnswerControllerIT extends BaseIT {

    @Test
    public void whenCreateThenReturnNoContent() {
        truncateDatabase();

        var research = createResearch();
        var question = createQuestion(UUID.fromString(research.getId()));
        var option = createOption(UUID.fromString(question.getId()));

        var answer = ResearchAnswerInputDTO.builder()
                .answer(QuestionAnswerInputDTO.builder()
                        .questionId(UUID.fromString(question.getId()))
                        .optionId(UUID.fromString(option.getId()))
                        .build())
                .build();

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(answer)
                .when()
                .post("/researches/{researchId}/answers", research.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void whenCreateWithoutAllResearchQuestionsThenReturnBadRequest() {
        truncateDatabase();

        var research = createResearch();
        var question = createQuestion(UUID.fromString(research.getId()));
        createQuestion(UUID.fromString(research.getId()));
        var option = createOption(UUID.fromString(question.getId()));

        var answer = ResearchAnswerInputDTO.builder()
                .answer(QuestionAnswerInputDTO.builder()
                        .questionId(UUID.fromString(question.getId()))
                        .optionId(UUID.fromString(option.getId()))
                        .build())
                .build();

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(answer)
                .when()
                .post("/researches/{researchId}/answers", research.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void whenCreateWithNonexistentResearchIdThenReturnBadRequest() {
        truncateDatabase();

        var research = createResearch();
        var question = createQuestion(UUID.fromString(research.getId()));
        var option = createOption(UUID.fromString(question.getId()));

        var answer = ResearchAnswerInputDTO.builder()
                .answer(QuestionAnswerInputDTO.builder()
                        .questionId(UUID.fromString(question.getId()))
                        .optionId(UUID.fromString(option.getId()))
                        .build())
                .build();

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(answer)
                .when()
                .post("/researches/{researchId}/answers", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void whenCreateWithNonexistentQuestionIdThenReturnBadRequest() {
        truncateDatabase();

        var research = createResearch();
        var question = createQuestion(UUID.fromString(research.getId()));
        var option = createOption(UUID.fromString(question.getId()));

        var answer = ResearchAnswerInputDTO.builder()
                .answer(QuestionAnswerInputDTO.builder()
                        .questionId(UUID.randomUUID())
                        .optionId(UUID.fromString(option.getId()))
                        .build())
                .build();

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(answer)
                .when()
                .post("/researches/{researchId}/answers", research.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void whenCreateWithNonexistentOptionIdThenReturnBadRequest() {
        truncateDatabase();

        var research = createResearch();
        var question = createQuestion(UUID.fromString(research.getId()));

        var answer = ResearchAnswerInputDTO.builder()
                .answer(QuestionAnswerInputDTO.builder()
                        .questionId(UUID.fromString(question.getId()))
                        .optionId(UUID.randomUUID())
                        .build())
                .build();

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(answer)
                .when()
                .post("/researches/{researchId}/answers", research.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void whenSearchWithoutParametersThenReturnAllAnswersForRequestedResearch() {
        truncateDatabase();

        var researchA = createResearch();
        var questionAA = createQuestion(UUID.fromString(researchA.getId()));
        var optionAAA = createOption(UUID.fromString(questionAA.getId()));
        var questionAB = createQuestion(UUID.fromString(researchA.getId()));
        var optionABA = createOption(UUID.fromString(questionAB.getId()));

        createAnswer(UUID.fromString(researchA.getId()), Map.of(
                UUID.fromString(questionAA.getId()), UUID.fromString(optionAAA.getId()),
                UUID.fromString(questionAB.getId()), UUID.fromString(optionABA.getId())));

        var researchB = createResearch();
        var questionBA = createQuestion(UUID.fromString(researchB.getId()));
        var optionBAA = createOption(UUID.fromString(questionBA.getId()));

        createAnswer(UUID.fromString(researchB.getId()), Map.of(
                UUID.fromString(questionBA.getId()), UUID.fromString(optionBAA.getId())));

        given()
                .accept(JSON)
                .when()
                .get("/researches/{researchId}/answers", researchA.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(2));
    }

    @Test
    public void whenSearchWithQuestionIdParameterThenReturnFiltered() {
        truncateDatabase();

        var research = createResearch();
        var questionA = createQuestion(UUID.fromString(research.getId()));
        var optionA = createOption(UUID.fromString(questionA.getId()));
        var questionB = createQuestion(UUID.fromString(research.getId()));
        var optionB = createOption(UUID.fromString(questionB.getId()));

        createAnswer(UUID.fromString(research.getId()), Map.of(
                UUID.fromString(questionA.getId()), UUID.fromString(optionA.getId()),
                UUID.fromString(questionB.getId()), UUID.fromString(optionB.getId())));

        given()
                .accept(JSON)
                .queryParam("questionId", questionA.getId())
                .when()
                .get("/researches/{researchId}/answers", research.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(1))
                .body("questionId", hasItem(questionA.getId()))
                .body("optionId", hasItem(optionA.getId()));
    }

    @Test
    public void whenSearchWithNonexistentQuestionIdThenReturnEmpty() {
        truncateDatabase();

        var research = createResearch();
        createQuestion(UUID.fromString(research.getId()));

        given()
                .accept(JSON)
                .queryParam("questionId", UUID.randomUUID())
                .when()
                .get("/researches/{researchId}/answers", research.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", empty());
    }

    @Test
    public void whenSearchWithDateParametersThenReturnFiltered() throws InterruptedException {
        truncateDatabase();

        var dateFrom = OffsetDateTime.now();

        var research = createResearch();
        var question = createQuestion(UUID.fromString(research.getId()));
        var option = createOption(UUID.fromString(question.getId()));

        createAnswer(UUID.fromString(research.getId()), Map.of(
                UUID.fromString(question.getId()), UUID.fromString(option.getId())));

        var dateTo = OffsetDateTime.now();

        Thread.sleep(1000);

        createAnswer(UUID.fromString(research.getId()), Map.of(
                UUID.fromString(question.getId()), UUID.fromString(option.getId())));

        given()
                .accept(JSON)
                .queryParam("dateFrom", ISO_DATE_TIME.format(dateFrom))
                .queryParam("dateTo", ISO_DATE_TIME.format(dateTo))
                .when()
                .get("/researches/{researchId}/answers", research.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(1))
                .body("questionId", hasItem(question.getId()))
                .body("optionId", hasItem(option.getId()));
    }

    @Test
    public void whenSearchWithDateParametersWithPreviousPeriodThenReturnFiltered() {
        truncateDatabase();

        var dateTo = OffsetDateTime.now();

        var research = createResearch();
        var question = createQuestion(UUID.fromString(research.getId()));
        var option = createOption(UUID.fromString(question.getId()));

        createAnswer(UUID.fromString(research.getId()), Map.of(
                UUID.fromString(question.getId()), UUID.fromString(option.getId())));

        given()
                .accept(JSON)
                .queryParam("dateTo", ISO_DATE_TIME.format(dateTo))
                .when()
                .get("/researches/{researchId}/answers", research.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(0));
    }

    @Test
    public void whenSearchWithDateParametersWithLaterPeriodThenReturnFiltered() {
        truncateDatabase();

        var research = createResearch();
        var question = createQuestion(UUID.fromString(research.getId()));
        var option = createOption(UUID.fromString(question.getId()));

        createAnswer(UUID.fromString(research.getId()), Map.of(
                UUID.fromString(question.getId()), UUID.fromString(option.getId())));

        var dateFrom = OffsetDateTime.now();

        given()
                .accept(JSON)
                .queryParam("dateFrom", ISO_DATE_TIME.format(dateFrom))
                .when()
                .get("/researches/{researchId}/answers", research.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(0));
    }

    @Test
    public void whenSearchWithNonexistentResearchIdThenReturnNotFound() {
        truncateDatabase();

        given()
                .accept(JSON)
                .when()
                .get("/researches/{researchId}/answers", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
