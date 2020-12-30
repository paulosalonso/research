package com.github.paulosalonso.research.usecase.option;

import com.github.paulosalonso.research.domain.Option;
import com.github.paulosalonso.research.usecase.port.OptionPort;
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
public class OptionCreateTest {

    @InjectMocks
    private OptionCreate optionCreate;

    @Mock
    private OptionPort optionPort;

    @Mock
    private QuestionPort questionPort;

    @Test
    public void givenAQuestionIdAndAnOptionWhenCreateThenCallPort() {
        var questionId = UUID.randomUUID();

        var toSave = Option.builder()
                .description("description")
                .build();

        optionCreate.create(questionId, toSave);

        ArgumentCaptor<Option> optionCaptor = ArgumentCaptor.forClass(Option.class);
        verify(optionPort).create(eq(questionId), optionCaptor.capture());

        var saved = optionCaptor.getValue();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getDescription()).isEqualTo(toSave.getDescription());

        verify(questionPort).getNextOptionSequence(questionId);
    }
}
