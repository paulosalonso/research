package com.github.paulosalonso.research.adapter.controller;

import com.github.paulosalonso.research.application.ResearchApplication;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = ResearchApplication.class)
public class BaseIT {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private int port;

    protected static final String ISO_8601_REGEX = "^(?:[1-9]\\d{3}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1\\d|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[1-9]\\d(?:0[48]|[2468][048]|[13579][26])|(?:[2468][048]|[13579][26])00)-02-29)T(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d(?:\\.\\d{1,9})?(?:Z|[+-][01]\\d:[0-5]\\d)$";

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/research/api";
    }

    protected void truncateDatabase() {
        truncateTable("answer");
        truncateTable("\"option\"");
        truncateTable("question");
        truncateTable("research");
    }

    protected void truncateTable(String tableName) {
        jdbcTemplate.execute("DELETE FROM " + tableName);
    }
}
