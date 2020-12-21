package com.github.paulosalonso.research.usecase.answer;

import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.domain.AnswerCriteria;
import com.github.paulosalonso.research.usecase.port.AnswerPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnswerReadTest {

    @InjectMocks
    private AnswerRead answerRead;

    @Mock
    private AnswerPort port;

    @Test
    public void givenAnAnswerCriteriaWhenSearchThenCallPort() {
        var criteria = AnswerCriteria.builder().build();

        // TODO - For mutation EMPTY_RETURN_VALUES coverage. Remove when implements integrated tests
        when(port.search(criteria)).thenReturn(List.of(Answer.builder().build()));

        var result = answerRead.search(criteria);

        // TODO - For mutation EMPTY_RETURN_VALUES coverage. Remove when implements integrated tests
        assertThat(result).hasSize(1);

        verify(port).search(criteria);
    }
}
