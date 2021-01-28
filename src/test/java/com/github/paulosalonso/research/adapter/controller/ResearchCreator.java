package com.github.paulosalonso.research.adapter.controller;

import com.github.paulosalonso.research.adapter.controller.dto.ResearchDTO;
import com.github.paulosalonso.research.adapter.controller.dto.ResearchInputDTO;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.github.paulosalonso.research.adapter.controller.BaseIT.givenAuthenticated;
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
        return givenAuthenticated()
                .contentType(JSON)
                .accept(JSON)
                .body(researchInputDTO)
                .post("/researches")
                .as(ResearchDTO.class);
    }
}
