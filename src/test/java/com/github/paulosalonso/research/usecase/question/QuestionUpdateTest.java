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
public class QuestionUpdateTest {

    @InjectMocks
    private QuestionUpdate questionUpdate;

    @Mock
    private QuestionPort port;

    @Test
    public void givenAResearchIdAndAQuestionWhenUpdateThenCallPort() {
        var id = UUID.randomUUID();

        var toUpdate = Question.builder()
                .description("description")
                .multiSelect(true)
                .build();

        var updated = Question.builder()
                .description("description")
                .multiSelect(true)
                .build();

        when(port.update(id, toUpdate)).thenReturn(updated);

        var result = questionUpdate.update(id, toUpdate);

        assertThat(result).isSameAs(updated);
        verify(port).update(id, toUpdate);
    }
}
