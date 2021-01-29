package com.github.paulosalonso.research.adapter.controller;

import com.github.paulosalonso.research.adapter.controller.dto.QuestionInputDTO;
import com.github.paulosalonso.research.adapter.controller.dto.ResearchAnswerInputDTO;
import com.github.paulosalonso.research.adapter.controller.dto.ResearchAnswerInputDTO.QuestionAnswerInputDTO;
import com.github.paulosalonso.research.adapter.controller.dto.ResearchInputDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import static com.github.paulosalonso.research.adapter.controller.AnswerCreator.createAnswer;
import static com.github.paulosalonso.research.adapter.controller.OptionCreator.createOption;
import static com.github.paulosalonso.research.adapter.controller.QuestionCreator.createQuestion;
import static com.github.paulosalonso.research.adapter.controller.ResearchCreator.createResearch;
import static io.restassured.http.ContentType.JSON;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.*;

public class AnswerControllerIT extends BaseIT {

    @Test
    public void whenCreateThenReturnNoContent() {
        truncateDatabase();

        var research = createResearch();
        var question = createQuestion(research.getId());
        var option = createOption(question.getId());

        var answer = ResearchAnswerInputDTO.builder()
                .answer(QuestionAnswerInputDTO.builder()
                        .questionId(question.getId())
                        .optionId(option.getId())
                        .build())
                .build();

        givenAuthenticatedUser()
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
        var questionA = createQuestion(research.getId());
        var questionB = createQuestion(research.getId());
        var option = createOption(questionA.getId());

        var answer = ResearchAnswerInputDTO.builder()
                .answer(QuestionAnswerInputDTO.builder()
                        .questionId(questionA.getId())
                        .optionId(option.getId())
                        .build())
                .build();

        givenAuthenticatedUser()
                .contentType(JSON)
                .accept(JSON)
                .body(answer)
                .when()
                .post("/researches/{researchId}/answers", research.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("The follow questions have not been answered: " + questionB.getId()))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void whenCreateWithNonexistentResearchIdThenReturnBadRequest() {
        truncateDatabase();

        var research = createResearch();
        var question = createQuestion(research.getId());
        var option = createOption(question.getId());

        var answer = ResearchAnswerInputDTO.builder()
                .answer(QuestionAnswerInputDTO.builder()
                        .questionId(question.getId())
                        .optionId(option.getId())
                        .build())
                .build();

        givenAuthenticatedUser()
                .contentType(JSON)
                .accept(JSON)
                .body(answer)
                .when()
                .post("/researches/{researchId}/answers", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", equalTo(HttpStatus.NOT_FOUND.value()))
                .body("message", equalTo("Requested resource not found"))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void whenCreateWithNonexistentQuestionIdThenReturnBadRequest() {
        truncateDatabase();

        var research = createResearch();
        var question = createQuestion(research.getId());
        var option = createOption(question.getId());

        var questionId = UUID.randomUUID();
        var answer = ResearchAnswerInputDTO.builder()
                .answer(QuestionAnswerInputDTO.builder()
                        .questionId(questionId)
                        .optionId(option.getId())
                        .build())
                .build();

        givenAuthenticatedUser()
                .contentType(JSON)
                .accept(JSON)
                .body(answer)
                .when()
                .post("/researches/{researchId}/answers", research.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("Question not found: " + questionId))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void whenCreateWithNonexistentOptionIdThenReturnBadRequest() {
        truncateDatabase();

        var research = createResearch();
        var question = createQuestion(research.getId());

        var optionId = UUID.randomUUID();
        var answer = ResearchAnswerInputDTO.builder()
                .answer(QuestionAnswerInputDTO.builder()
                        .questionId(question.getId())
                        .optionId(optionId)
                        .build())
                .build();

        givenAuthenticatedUser()
                .contentType(JSON)
                .accept(JSON)
                .body(answer)
                .when()
                .post("/researches/{researchId}/answers", research.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("Option not found: " + optionId))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void whenCreateForNotStartedResearchThenReturnBadRequest() {
        truncateDatabase();

        var research = createResearch(ResearchInputDTO.builder()
                .title("title")
                .startsOn(OffsetDateTime.now().plusHours(1))
                .build());

        var question = createQuestion(research.getId());
        var option = createOption(question.getId());

        var answer = ResearchAnswerInputDTO.builder()
                .answer(QuestionAnswerInputDTO.builder()
                        .questionId(question.getId())
                        .optionId(option.getId())
                        .build())
                .build();

        givenAuthenticatedUser()
                .contentType(JSON)
                .accept(JSON)
                .body(answer)
                .when()
                .post("/researches/{researchId}/answers", research.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("Research is not started"))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void whenCreateForFinalizedResearchThenReturnBadRequest() {
        truncateDatabase();

        var research = createResearch(ResearchInputDTO.builder()
                .title("title")
                .startsOn(OffsetDateTime.now().minusHours(2))
                .endsOn(OffsetDateTime.now().minusHours(1))
                .build());

        var question = createQuestion(research.getId());
        var option = createOption(question.getId());

        var answer = ResearchAnswerInputDTO.builder()
                .answer(QuestionAnswerInputDTO.builder()
                        .questionId(question.getId())
                        .optionId(option.getId())
                        .build())
                .build();

        givenAuthenticatedUser()
                .contentType(JSON)
                .accept(JSON)
                .body(answer)
                .when()
                .post("/researches/{researchId}/answers", research.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("Research is finalized"))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void givenAQuestionMultipleSelectableWhenCreateWithMultipleOptionsSelectionThenReturnOk() {
        var research = createResearch();
        var question = createQuestion(research.getId(), QuestionInputDTO.builder()
                .description("description")
                .multiSelect(true)
                .build());
        var optionA = createOption(question.getId());
        var optionB = createOption(question.getId());

        var answer = ResearchAnswerInputDTO.builder()
                .answer(QuestionAnswerInputDTO.builder()
                        .questionId(question.getId())
                        .optionId(optionA.getId())
                        .build())
                .answer(QuestionAnswerInputDTO.builder()
                        .questionId(question.getId())
                        .optionId(optionB.getId())
                        .build())
                .build();

        givenAuthenticatedUser()
                .contentType(JSON)
                .accept(JSON)
                .body(answer)
                .when()
                .post("/researches/{researchId}/answers", research.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void givenAQuestionNotMultipleSelectableWhenCreateWithMultipleOptionsSelectionThenReturnBadRequest() {
        var research = createResearch();
        var question = createQuestion(research.getId(), QuestionInputDTO.builder()
                .description("description")
                .multiSelect(false)
                .build());
        var optionA = createOption(question.getId());
        var optionB = createOption(question.getId());

        var answer = ResearchAnswerInputDTO.builder()
                .answer(QuestionAnswerInputDTO.builder()
                        .questionId(question.getId())
                        .optionId(optionA.getId())
                        .build())
                .answer(QuestionAnswerInputDTO.builder()
                        .questionId(question.getId())
                        .optionId(optionB.getId())
                        .build())
                .build();

        givenAuthenticatedUser()
                .contentType(JSON)
                .accept(JSON)
                .body(answer)
                .when()
                .post("/researches/{researchId}/answers", research.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("The question does not allow the selection of various options: " + question.getId()))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void whenSearchWithoutParametersThenReturnAllAnswersSummarized() {
        truncateDatabase();

        var research = createResearch();
        var questionA = createQuestion(research.getId());
        var optionAA = createOption(questionA.getId());
        var optionAB = createOption(questionA.getId());
        var questionB = createQuestion(research.getId());
        var optionBA = createOption(questionB.getId());
        var optionBB = createOption(questionB.getId());
        var questionC = createQuestion(research.getId());
        var optionCA = createOption(questionC.getId());
        var optionCB = createOption(questionC.getId());
        var optionCC = createOption(questionC.getId());

        createAnswer(research.getId(), Map.of(
                questionA.getId(), optionAA.getId(),
                questionB.getId(), optionBA.getId(),
                questionC.getId(), optionCA.getId()));

        createAnswer(research.getId(), Map.of(
                questionA.getId(), optionAA.getId(),
                questionB.getId(), optionBA.getId(),
                questionC.getId(), optionCA.getId()));

        createAnswer(research.getId(), Map.of(
                questionA.getId(), optionAB.getId(),
                questionB.getId(), optionBB.getId(),
                questionC.getId(), optionCB.getId()));

        givenAuthenticatedUser()
                .accept(JSON)
                .when()
                .get("/researches/{researchId}/answers", research.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(research.getId().toString()))
                .body("title", equalTo(research.getTitle()))
                .body("criteria", notNullValue())
                .body("criteria.dateFrom", nullValue())
                .body("criteria.dateTo", nullValue())
                .body("criteria.questionId", nullValue())
                .body("questions", hasSize(3))
                .body("questions.id", contains(questionA.getId().toString(), questionB.getId().toString(), questionC.getId().toString()))
                .body("questions.sequence", contains(questionA.getSequence(), questionB.getSequence(), questionC.getSequence()))
                .body("questions.description", contains(questionA.getDescription(), questionB.getDescription(), questionC.getDescription()))
                .body("questions[0].options", hasSize(2))
                .body("questions[0].options.id", contains(optionAA.getId().toString(), optionAB.getId().toString()))
                .body("questions[0].options.sequence", contains(optionAA.getSequence(), optionAB.getSequence()))
                .body("questions[0].options.description", contains(optionAA.getDescription(), optionAB.getDescription()))
                .body("questions[0].options.amount", contains(2, 1))
                .body("questions[1].options", hasSize(2))
                .body("questions[1].options.id", contains(optionBA.getId().toString(), optionBB.getId().toString()))
                .body("questions[1].options.sequence", contains(optionBA.getSequence(), optionBB.getSequence()))
                .body("questions[1].options.description", contains(optionBA.getDescription(), optionBB.getDescription()))
                .body("questions[1].options.amount", contains(2, 1))
                .body("questions[2].options", hasSize(3))
                .body("questions[2].options.id", contains(optionCA.getId().toString(), optionCB.getId().toString(), optionCC.getId().toString()))
                .body("questions[2].options.sequence", contains(optionCA.getSequence(), optionCB.getSequence(), optionCC.getSequence()))
                .body("questions[2].options.description", contains(optionCA.getDescription(), optionCB.getDescription(), optionCC.getDescription()))
                .body("questions[2].options.amount", contains(2, 1, 0));
    }

    @Test
    public void whenSearchWithQuestionIdParameterThenReturnFiltered() {
        truncateDatabase();

        var research = createResearch();
        var questionA = createQuestion(research.getId());
        var optionAA = createOption(questionA.getId());
        createOption(questionA.getId());
        var questionB = createQuestion(research.getId());
        var optionBA = createOption(questionB.getId());

        createAnswer(research.getId(), Map.of(
                questionA.getId(), optionAA.getId(),
                questionB.getId(), optionBA.getId()));

        givenAuthenticatedUser()
                .accept(JSON)
                .queryParam("questionId", questionA.getId())
                .when()
                .get("/researches/{researchId}/answers", research.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("criteria.dateFrom", nullValue())
                .body("criteria.dateTo", nullValue())
                .body("criteria.questionId", equalTo(questionA.getId().toString()))
                .body("questions[0].options[0].amount", equalTo(1))
                .body("questions[0].options[1].amount", equalTo(0))
                .body("questions[1].options[0].amount", equalTo(0));
    }

    @Test
    public void whenSearchWithNonexistentQuestionIdThenReturnEmpty() {
        truncateDatabase();

        var research = createResearch();
        var question = createQuestion(research.getId());
        var option = createOption(question.getId());

        createAnswer(research.getId(), Map.of(
                question.getId(), option.getId()));

        var questionId = UUID.randomUUID().toString();

        givenAuthenticatedUser()
                .accept(JSON)
                .queryParam("questionId", questionId)
                .when()
                .get("/researches/{researchId}/answers", research.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("criteria.dateFrom", nullValue())
                .body("criteria.dateTo", nullValue())
                .body("criteria.questionId", equalTo(questionId))
                .body("questions[0].options[0].amount", equalTo(0));
    }

    @Test
    public void whenSearchWithDateParametersThenReturnFiltered() throws InterruptedException {
        truncateDatabase();

        var dateFrom = OffsetDateTime.now();

        var research = createResearch();
        var question = createQuestion(research.getId());
        var option = createOption(question.getId());

        createAnswer(research.getId(), Map.of(
                question.getId(), option.getId()));

        var dateTo = OffsetDateTime.now();

        Thread.sleep(1000);

        createAnswer(research.getId(), Map.of(
                question.getId(), option.getId()));

        givenAuthenticatedUser()
                .accept(JSON)
                .queryParam("dateFrom", ISO_DATE_TIME.format(dateFrom))
                .queryParam("dateTo", ISO_DATE_TIME.format(dateTo))
                .when()
                .get("/researches/{researchId}/answers", research.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("criteria.dateFrom", equalTo(ISO_DATE_TIME.format(dateFrom)))
                .body("criteria.dateTo", equalTo(ISO_DATE_TIME.format(dateTo)))
                .body("criteria.questionId", nullValue())
                .body("questions[0].options[0].amount", equalTo(1));
    }

    @Test
    public void whenSearchWithDateParametersWithPreviousPeriodThenReturnFiltered() {
        truncateDatabase();

        var dateTo = OffsetDateTime.now();

        var research = createResearch();
        var question = createQuestion(research.getId());
        var option = createOption(question.getId());

        createAnswer(research.getId(), Map.of(
                question.getId(), option.getId()));

        givenAuthenticatedUser()
                .accept(JSON)
                .queryParam("dateTo", ISO_DATE_TIME.format(dateTo))
                .when()
                .get("/researches/{researchId}/answers", research.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("criteria.dateFrom", nullValue())
                .body("criteria.dateTo", equalTo(ISO_DATE_TIME.format(dateTo)))
                .body("criteria.questionId", nullValue())
                .body("questions[0].options[0].amount", equalTo(0));
    }

    @Test
    public void whenSearchWithDateParametersWithLaterPeriodThenReturnFiltered() {
        truncateDatabase();

        var research = createResearch();
        var question = createQuestion(research.getId());
        var option = createOption(question.getId());

        createAnswer(research.getId(), Map.of(
                question.getId(), option.getId()));

        var dateFrom = OffsetDateTime.now();

        givenAuthenticatedUser()
                .accept(JSON)
                .queryParam("dateFrom", ISO_DATE_TIME.format(dateFrom))
                .when()
                .get("/researches/{researchId}/answers", research.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("criteria.dateFrom", equalTo(ISO_DATE_TIME.format(dateFrom)))
                .body("criteria.dateTo", nullValue())
                .body("criteria.questionId", nullValue())
                .body("questions[0].options[0].amount", equalTo(0));
    }

    @Test
    public void whenSearchWithAllParametersThenReturnFiltered() throws InterruptedException {
        truncateDatabase();

        var dateFrom = OffsetDateTime.now();

        var research = createResearch();
        var questionA = createQuestion(research.getId());
        var optionAA = createOption(questionA.getId());
        var questionB = createQuestion(research.getId());
        var optionBA = createOption(questionB.getId());

        createAnswer(research.getId(), Map.of(
                questionA.getId(), optionAA.getId(),
                questionB.getId(), optionBA.getId()));

        var dateTo = OffsetDateTime.now();

        Thread.sleep(1000);

        createAnswer(research.getId(), Map.of(
                questionA.getId(), optionAA.getId(),
                questionB.getId(), optionBA.getId()));

        givenAuthenticatedUser()
                .accept(JSON)
                .queryParam("dateFrom", ISO_DATE_TIME.format(dateFrom))
                .queryParam("dateTo", ISO_DATE_TIME.format(dateTo))
                .queryParam("questionId", questionA.getId())
                .when()
                .get("/researches/{researchId}/answers", research.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("criteria.dateFrom", equalTo(ISO_DATE_TIME.format(dateFrom)))
                .body("criteria.dateTo", equalTo(ISO_DATE_TIME.format(dateTo)))
                .body("criteria.questionId", equalTo(questionA.getId().toString()))
                .body("questions[0].options[0].amount", equalTo(1))
                .body("questions[1].options[0].amount", equalTo(0));
    }

    @Test
    public void whenSearchWithNonexistentResearchIdThenReturnNotFound() {
        truncateDatabase();

        givenAuthenticatedUser()
                .accept(JSON)
                .when()
                .get("/researches/{researchId}/answers", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", equalTo(HttpStatus.NOT_FOUND.value()))
                .body("message", equalTo("Requested resource not found"))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }
}
