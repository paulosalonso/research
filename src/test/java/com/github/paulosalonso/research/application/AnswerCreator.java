package com.github.paulosalonso.research.application;

import com.github.paulosalonso.research.application.dto.AnswerDTO;
import com.github.paulosalonso.research.application.dto.AnswerInputDTO;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class AnswerCreator {

    public static AnswerDTO createAnswer(UUID researchId, UUID questionId, UUID optionId) {
        var answer = AnswerInputDTO.builder()
                .questionId(questionId)
                .optionId(optionId)
                .build();

        return given()
                .contentType(JSON)
                .accept(JSON)
                .body(answer)
                .post("/researches/{researchId}/answers", researchId)
                .as(AnswerDTO.class);
    }
}
