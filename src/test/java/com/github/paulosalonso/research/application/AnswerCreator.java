package com.github.paulosalonso.research.application;

import com.github.paulosalonso.research.application.dto.ResearchAnswerInputDTO;
import com.github.paulosalonso.research.application.dto.ResearchAnswerInputDTO.QuestionAnswerInputDTO;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class AnswerCreator {

    public static void createAnswer(UUID researchId, UUID questionId, UUID optionId) {
        var answer = ResearchAnswerInputDTO.builder()
                .answer(QuestionAnswerInputDTO.builder()
                        .questionId(questionId)
                        .optionId(optionId)
                        .build())
                .build();

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(answer)
                .post("/researches/{researchId}/answers", researchId);
    }
}
