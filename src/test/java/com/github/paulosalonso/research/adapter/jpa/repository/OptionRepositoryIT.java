package com.github.paulosalonso.research.adapter.jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
public class OptionRepositoryIT {

    @Autowired
    private OptionRepository repository;

    @Test
    public void whenFindByQuestionIdThenDoesNotThrowAnyException() {
        assertThatCode(() -> repository.findByQuestionId("question-id")).doesNotThrowAnyException();
    }



    @Test
    public void whenFindByQuestionIdAndIdThenDoesNotThrowAnyException() {
        assertThatCode(() -> repository.findByQuestionIdAndId("question-id", "option-id"))
                .doesNotThrowAnyException();
    }
}
