package com.github.paulosalonso.research.adapter.controller;

import com.github.paulosalonso.research.adapter.controller.dto.OptionInputDTO;
import com.github.paulosalonso.research.adapter.controller.dto.QuestionInputDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.github.paulosalonso.research.adapter.controller.creator.OptionCreator.createOption;
import static com.github.paulosalonso.research.adapter.controller.creator.QuestionCreator.createQuestion;
import static com.github.paulosalonso.research.adapter.controller.creator.ResearchCreator.createResearch;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;

public class OptionControllerIT extends BaseIT {

    @Test
    public void whenGetThenReturnOk() {
        var research = createResearch();
        var question = createQuestion(research.getId());

        createOption(question.getId());

        var body = OptionInputDTO.builder()
                .description("description")
                .build();

        String optionId = given()
                .contentType(JSON)
                .accept(JSON)
                .body(body)
                .post("/questions/{questionId}/options", question.getId())
                .path("id");

        given()
                .accept(JSON)
                .when()
                .get("/questions/{questionId}/options/{optionId}", question.getId(), optionId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(optionId))
                .body("description", equalTo(body.getDescription()));
    }

    @Test
    public void givenANonexistentQuestionIdWhenGetThenReturnNotFound() {
        var research = createResearch();
        var question = createQuestion(research.getId());
        var option = createOption(question.getId());

        given()
                .accept(JSON)
                .when()
                .get("/questions/{questionId}/options/{optionId}", UUID.randomUUID(), option.getId())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", equalTo(HttpStatus.NOT_FOUND.value()))
                .body("message", equalTo("Requested resource not found"))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void givenANonexistentOptionIdWhenGetThenReturnNotFound() {
        var research = createResearch();
        var question = createQuestion(research.getId());

        given()
                .accept(JSON)
                .when()
                .get("/questions/{questionId}/options/{optionId}", question.getId(), UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", equalTo(HttpStatus.NOT_FOUND.value()))
                .body("message", equalTo("Requested resource not found"))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void givenAnInvalidQuestionUUIDWhenGetThenReturnBadRequest() {
        given()
                .contentType(JSON)
                .accept(JSON)
                .when()
                .get("/questions/{questionId}/options/{optionId}", "invalid-uuid", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("'invalid-uuid' is an invalid value for the 'questionId' URL parameter. Required type is 'UUID'."))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void givenAnInvalidOptionUUIDWhenGetThenReturnBadRequest() {
        given()
                .contentType(JSON)
                .accept(JSON)
                .when()
                .get("/questions/{questionId}/options/{optionId}", UUID.randomUUID(), "invalid-uuid")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("'invalid-uuid' is an invalid value for the 'optionId' URL parameter. Required type is 'UUID'."))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void whenSearchWithoutParametersThenReturnAll() {
        truncateDatabase();

        var research = createResearch();
        var question = createQuestion(research.getId());

        createOption(question.getId());
        createOption(question.getId());

        given()
                .accept(JSON)
                .when()
                .get("/questions/{questionId}/options", question.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(2));
    }

    @Test
    public void whenSearchWithDescriptionParameterThenReturnFiltered() {
        truncateDatabase();

        var research = createResearch();
        var question = createQuestion(research.getId());

        createOption(question.getId(), OptionInputDTO.builder()
                .description("description-a")
                .build());

        var optionB = createOption(question.getId(), OptionInputDTO.builder()
                .description("description-b")
                .build());

        given()
                .accept(JSON)
                .queryParam("description", optionB.getDescription())
                .when()
                .get("/questions/{questionId}/options", question.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(1))
                .body("id", contains(optionB.getId().toString()));
    }

    @Test
    public void whenCreateThenReturnCreated() {
        var research = createResearch();
        var question = createQuestion(research.getId());

        var body = OptionInputDTO.builder()
                .description("description")
                .build();

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(body)
                .when()
                .post("/questions/{questionId}/options", question.getId())
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("description", equalTo(body.getDescription()));
    }

    @Test
    public void whenCreateWithNonexistentQuestionIdThenReturnNotFound() {
        var body = QuestionInputDTO.builder()
                .description("description")
                .multiSelect(false)
                .build();

        var questionId = UUID.randomUUID();

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(body)
                .when()
                .post("/questions/{questionId}/options", questionId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", equalTo(HttpStatus.NOT_FOUND.value()))
                .body("message", equalTo("Requested resource not found"))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void whenCreateWithNullRequiredValueThenReturnBadRequest() {
        var research = createResearch();
        var question = createQuestion(research.getId());

        given()
                .contentType(JSON)
                .accept(JSON)
                .header("Accept-Language", "en-US")
                .body(OptionInputDTO.builder().build())
                .when()
                .post("/questions/{questionId}/options", question.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("Invalid field(s)"))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("fields", hasSize(1))
                .body("fields[0].name", equalTo("description"))
                .body("fields[0].message", equalTo("must not be blank"));
    }

    @Test
    public void whenUpdateThenReturnOk() {
        var research = createResearch();
        var question = createQuestion(research.getId());
        var option = createOption(question.getId());

        var updateBody = OptionInputDTO.builder()
                .description(question.getDescription() + " updated")
                .build();

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(updateBody)
                .when()
                .put("questions/{questionId}/options/{optionId}", question.getId(), option.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(option.getId().toString()))
                .body("description", equalTo(updateBody.getDescription()));
    }

    @Test
    public void givenANonexistentQuestionIdWhenUpdateThenReturnNotFound() {
        var research = createResearch();
        var question = createQuestion(research.getId());
        var option = createOption(question.getId());

        var updateBody = OptionInputDTO.builder()
                .description(question.getDescription() + " updated")
                .build();

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(updateBody)
                .when()
                .put("questions/{questionId}/options/{optionId}", UUID.randomUUID(), option.getId())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", equalTo(HttpStatus.NOT_FOUND.value()))
                .body("message", equalTo("Requested resource not found"))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void givenANonexistentOptionIdWhenUpdateThenReturnNotFound() {
        var research = createResearch();
        var question = createQuestion(research.getId());

        var updateBody = QuestionInputDTO.builder()
                .description(question.getDescription() + " updated")
                .multiSelect(!question.getMultiSelect())
                .build();

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(updateBody)
                .when()
                .put("questions/{questionId}/options/{optionId}", question.getId(), UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", equalTo(HttpStatus.NOT_FOUND.value()))
                .body("message", equalTo("Requested resource not found"))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void whenUpdateWithNullRequiredValueThenReturnBadRequest() {
        var research = createResearch();
        var question = createQuestion(research.getId());
        var option = createOption(question.getId());

        given()
                .contentType(JSON)
                .accept(JSON)
                .header("Accept-Language", "en-US")
                .body(OptionInputDTO.builder().build())
                .when()
                .put("questions/{questionId}/options/{optionId}", question.getId(), option.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("Invalid field(s)"))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("fields", hasSize(1))
                .body("fields[0].name", equalTo("description"))
                .body("fields[0].message", equalTo("must not be blank"));
    }

    @Test
    public void givenAnInvalidQuestionUUIDWhenUpdateThenReturnBadRequest() {
        given()
                .contentType(JSON)
                .accept(JSON)
                .body(OptionInputDTO.builder().build())
                .when()
                .put("questions/{questionId}/options/{optionId}", "invalid-uuid", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("'invalid-uuid' is an invalid value for the 'questionId' URL parameter. Required type is 'UUID'."))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void givenAnInvalidOptionUUIDWhenUpdateThenReturnBadRequest() {
        given()
                .contentType(JSON)
                .accept(JSON)
                .body(OptionInputDTO.builder().build())
                .when()
                .put("questions/{questionId}/options/{optionId}", UUID.randomUUID(), "invalid-uuid")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("'invalid-uuid' is an invalid value for the 'optionId' URL parameter. Required type is 'UUID'."))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void whenDeleteThenReturnNoContent() {
        var research = createResearch();
        var question = createQuestion(research.getId());
        var option = createOption(question.getId());

        given()
                .contentType(JSON)
                .accept(JSON)
                .when()
                .delete("/questions/{questionId}/options/{optionId}", question.getId(), option.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void givenANonexistentQuestionIdWhenDeleteThenReturnNotFound() {
        var research = createResearch();
        var question = createQuestion(research.getId());
        var option = createOption(question.getId());

        given()
                .contentType(JSON)
                .accept(JSON)
                .when()
                .delete("/questions/{questionId}/options/{optionId}", UUID.randomUUID(), option.getId())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", equalTo(HttpStatus.NOT_FOUND.value()))
                .body("message", equalTo("Requested resource not found"))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void givenANonexistentOptionIdWhenDeleteThenReturnNotFound() {
        var research = createResearch();
        var question = createQuestion(research.getId());

        given()
                .contentType(JSON)
                .accept(JSON)
                .when()
                .delete("/questions/{questionId}/options/{optionId}", question.getId(), UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", equalTo(HttpStatus.NOT_FOUND.value()))
                .body("message", equalTo("Requested resource not found"))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void givenAnInvalidQuestionUUIDWhenDeleteThenReturnBadRequest() {
        given()
                .contentType(JSON)
                .accept(JSON)
                .when()
                .delete("/questions/{questionId}/options/{optionId}", "invalid-uuid", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("'invalid-uuid' is an invalid value for the 'questionId' URL parameter. Required type is 'UUID'."))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void givenAnInvalidOptionUUIDWhenDeleteThenReturnBadRequest() {
        given()
                .contentType(JSON)
                .accept(JSON)
                .when()
                .delete("/questions/{questionId}/options/{optionId}", UUID.randomUUID(), "invalid-uuid")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("'invalid-uuid' is an invalid value for the 'optionId' URL parameter. Required type is 'UUID'."))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }
}
