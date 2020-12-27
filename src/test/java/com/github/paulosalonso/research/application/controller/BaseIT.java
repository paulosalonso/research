package com.github.paulosalonso.research.application.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class BaseIT {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private int port;

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
