package com.github.paulosalonso.research.application;

import com.github.paulosalonso.research.application.dto.QuestionDTO;
import com.github.paulosalonso.research.application.dto.QuestionInputDTO;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class QuestionCreator {

    public static QuestionDTO createQuestion(UUID researchId) {
        var body = QuestionInputDTO.builder()
                .description("description")
                .multiSelect(false)
                .build();

        return createQuestion(researchId, body);
    }

    public static QuestionDTO createQuestion(UUID researchId, QuestionInputDTO questionInputDTO) {
        return given()
                .contentType(JSON)
                .accept(JSON)
                .body(questionInputDTO)
                .post("/researches/{researchId}/questions", researchId)
                .as(QuestionDTO.class);
    }
}
