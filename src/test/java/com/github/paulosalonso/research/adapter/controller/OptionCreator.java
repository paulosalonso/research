package com.github.paulosalonso.research.adapter.controller;

import com.github.paulosalonso.research.adapter.controller.dto.OptionDTO;
import com.github.paulosalonso.research.adapter.controller.dto.OptionInputDTO;

import java.util.UUID;

import static com.github.paulosalonso.research.adapter.controller.BaseIT.givenAuthenticated;
import static io.restassured.http.ContentType.JSON;

public class OptionCreator {

    public static OptionDTO createOption(UUID questionId) {
        var body = OptionInputDTO.builder()
                .description("description " + questionId)
                .build();

        return createOption(questionId, body);
    }

    public static OptionDTO createOption(UUID questionId, OptionInputDTO optionInputDTO) {
        return givenAuthenticated()
                .contentType(JSON)
                .accept(JSON)
                .body(optionInputDTO)
                .post("/questions/{questionId}/options", questionId)
                .as(OptionDTO.class);
    }
}
