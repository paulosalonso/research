package com.github.paulosalonso.research.adapter.jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
public class QuestionRepositoryIT {

    @Autowired
    private QuestionRepository repository;

    @Test
    public void whenFindByResearchIdThenDoesNotThrowAnyException() {
        assertThatCode(() -> repository.findByResearchId("research-id")).doesNotThrowAnyException();
    }

    @Test
    public void whenFindByResearchIdAndIdThenDoesNotThrowAnyException() {
        assertThatCode(() -> repository.findByResearchIdAndId("research-id", "question-id"))
                .doesNotThrowAnyException();
    }
}
