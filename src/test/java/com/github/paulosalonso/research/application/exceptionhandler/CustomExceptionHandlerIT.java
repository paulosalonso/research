package com.github.paulosalonso.research.application.exceptionhandler;

import com.github.paulosalonso.research.adapter.controller.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.matchesRegex;

public class CustomExceptionHandlerIT extends BaseIT {

    @Test
    public void whenBodyContainsAnInvalidValueThenReturnBadRequest() {
        var body = "{\"title\":\"title\", \"startsOn\":\"invalid-date\"}";

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(body)
                .when()
                .post("/researches")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("'invalid-date' is not a valid value for the 'startsOn' property. The required type is OffsetDateTime."))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void whenBodyIsInvalidThenReturnBadRequest() {
        var body = "{'title\":\"title\", \"startsOn\":\"invalid-date\"}";

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(body)
                .when()
                .post("/researches")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", startsWith("Invalid request body. Original error message: "))
                .body("timestamp", matchesRegex(ISO_8601_REGEX))
                .body("$", not(hasKey("fields")));
    }
}
