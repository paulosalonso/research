package com.github.paulosalonso.research.usecase.question;

import com.github.paulosalonso.research.domain.Question;
import com.github.paulosalonso.research.usecase.port.QuestionPort;
import com.github.paulosalonso.research.usecase.port.ResearchPort;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuestionCreateTest {

    private static final String TENANT = "tenant";

    @InjectMocks
    private QuestionCreate questionCreate;

    @Mock
    private QuestionPort questionPort;

    @Mock
    private ResearchPort researchPort;

    @Test
    public void givenAResearchIdAndAQuestionWhenCreateThenCallPort() {
        var id = UUID.randomUUID();

        var toSave = Question.builder()
                .description("description")
                .multiSelect(true)
                .build();

        when(researchPort.getNextQuestionSequence(id)).thenReturn(1);

        ArgumentCaptor<Question> questionCaptor = ArgumentCaptor.forClass(Question.class);
        questionCreate.create(id, TENANT, toSave);

        verify(researchPort).getNextQuestionSequence(id);
        verify(questionPort).create(eq(id), eq(TENANT), questionCaptor.capture());

        var saved = questionCaptor.getValue();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getSequence()).isEqualTo(1);
        assertThat(saved.getDescription()).isEqualTo(toSave.getDescription());
        assertThat(saved.getMultiSelect()).isEqualTo(toSave.getMultiSelect());
    }
}
