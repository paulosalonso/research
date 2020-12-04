package com.github.paulosalonso.research.usecase.question;

import com.github.paulosalonso.research.domain.Question;
import com.github.paulosalonso.research.usecase.port.QuestionPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class QuestionCreateTest {

    @InjectMocks
    private QuestionCreate questionCreate;

    @Mock
    private QuestionPort port;

    @Test
    public void givenAResearchIdAndAQuestionWhenCreateThenCallPort() {
        var id = UUID.randomUUID();

        var toSave = Question.builder()
                .description("description")
                .multiSelect(true)
                .build();

        ArgumentCaptor<Question> questionCaptor = ArgumentCaptor.forClass(Question.class);
        questionCreate.create(id, toSave);

        verify(port).create(eq(id), questionCaptor.capture());

        var saved = questionCaptor.getValue();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getDescription()).isEqualTo(toSave.getDescription());
        assertThat(saved.getMultiSelect()).isEqualTo(toSave.getMultiSelect());
    }
}
