package com.github.paulosalonso.research.application;

import com.github.paulosalonso.research.application.dto.ResearchDTO;
import com.github.paulosalonso.research.application.dto.ResearchInputDTO;

import java.time.OffsetDateTime;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class ResearchCreator {

    public static ResearchDTO createResearch() {
        var uuid = UUID.randomUUID();

        var body = ResearchInputDTO.builder()
                .title("title " + uuid)
                .description("description " + uuid)
                .startsOn(OffsetDateTime.now())
                .endsOn(OffsetDateTime.now().plusMonths(1))
                .build();

        return createResearch(body);
    }

    public static ResearchDTO createResearch(ResearchInputDTO researchInputDTO) {
        return given()
                .contentType(JSON)
                .accept(JSON)
                .body(researchInputDTO)
                .post("/researches")
                .as(ResearchDTO.class);
    }
}
