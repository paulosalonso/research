package com.github.paulosalonso.research.adapter.controller;

import com.github.paulosalonso.research.adapter.controller.dto.QuestionDTO;
import com.github.paulosalonso.research.adapter.controller.dto.QuestionInputDTO;

import java.util.Random;
import java.util.UUID;

import static com.github.paulosalonso.research.adapter.controller.BaseIT.givenAuthenticated;
import static io.restassured.http.ContentType.JSON;

public class QuestionCreator {

    private static final Random RANDOM = new Random();

    public static QuestionDTO createQuestion(UUID researchId) {
        var body = QuestionInputDTO.builder()
                .description("description " + researchId)
                .multiSelect(RANDOM.nextBoolean())
                .build();

        return createQuestion(researchId, body);
    }

    public static QuestionDTO createQuestion(UUID researchId, QuestionInputDTO questionInputDTO) {
        return givenAuthenticated()
                .contentType(JSON)
                .accept(JSON)
                .body(questionInputDTO)
                .post("/researches/{researchId}/questions", researchId)
                .as(QuestionDTO.class);
    }
}
