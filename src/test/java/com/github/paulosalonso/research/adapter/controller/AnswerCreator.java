package com.github.paulosalonso.research.adapter.controller;

import com.github.paulosalonso.research.adapter.controller.dto.ResearchAnswerInputDTO;
import com.github.paulosalonso.research.adapter.controller.dto.ResearchAnswerInputDTO.QuestionAnswerInputDTO;

import java.util.Map;
import java.util.UUID;

import static com.github.paulosalonso.research.adapter.controller.BaseIT.givenAuthenticatedAdmin;
import static io.restassured.http.ContentType.JSON;

public class AnswerCreator {

    public static void createAnswer(UUID researchId, Map<UUID, UUID> answers) {
        var builder = ResearchAnswerInputDTO.builder();

        answers.keySet()
                .forEach(questionId -> builder.answer(QuestionAnswerInputDTO.builder()
                        .questionId(questionId)
                        .optionId(answers.get(questionId))
                        .build()));

        givenAuthenticatedAdmin()
                .contentType(JSON)
                .accept(JSON)
                .body(builder.build())
                .post("/researches/{researchId}/answers", researchId);
    }
}
