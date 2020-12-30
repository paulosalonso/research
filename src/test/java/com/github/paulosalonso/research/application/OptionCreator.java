package com.github.paulosalonso.research.application;

import com.github.paulosalonso.research.application.dto.OptionDTO;
import com.github.paulosalonso.research.application.dto.OptionInputDTO;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class OptionCreator {

    public static OptionDTO createOption(UUID questionId) {
        var body = OptionInputDTO.builder()
                .description("description " + questionId)
                .build();

        return createOption(questionId, body);
    }

    public static OptionDTO createOption(UUID questionId, OptionInputDTO optionInputDTO) {
        return given()
                .contentType(JSON)
                .accept(JSON)
                .body(optionInputDTO)
                .post("/questions/{questionId}/options", questionId)
                .as(OptionDTO.class);
    }
}
