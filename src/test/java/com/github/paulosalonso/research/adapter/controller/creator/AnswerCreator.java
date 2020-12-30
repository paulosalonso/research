package com.github.paulosalonso.research.adapter.controller.creator;

import com.github.paulosalonso.research.adapter.controller.dto.ResearchAnswerInputDTO;
import com.github.paulosalonso.research.adapter.controller.dto.ResearchAnswerInputDTO.QuestionAnswerInputDTO;

import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class AnswerCreator {

    public static void createAnswer(UUID researchId, Map<UUID, UUID> answers) {
        var builder = ResearchAnswerInputDTO.builder();

        answers.keySet()
                .forEach(questionId -> builder.answer(QuestionAnswerInputDTO.builder()
                        .questionId(questionId)
                        .optionId(answers.get(questionId))
                        .build()));

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(builder.build())
                .post("/researches/{researchId}/answers", researchId);
    }
}
