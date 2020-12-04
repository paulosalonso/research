package com.github.paulosalonso.research.usecase.question;

import com.github.paulosalonso.research.domain.Question;
import com.github.paulosalonso.research.usecase.port.QuestionPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuestionCreateTest {

    @InjectMocks
    private QuestionCreate questionCreate;

    @Mock
    private QuestionPort port;

    @Test
    public void givenAResearchIdAndAQuestionWhenCreateThenCallPort() {
        var id = UUID.randomUUID();
        var toSave = Question.builder().build();
        var saved = Question.builder().build();

        when(port.create(id, toSave)).thenReturn(saved);

        var result = questionCreate.create(id, toSave);

        assertThat(result).isSameAs(saved);
        verify(port).create(id, toSave);
    }
}
