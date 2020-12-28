package com.github.paulosalonso.research.usecase.answer;

import com.github.paulosalonso.research.domain.AnswerCriteria;
import com.github.paulosalonso.research.usecase.port.AnswerPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AnswerReadTest {

    @InjectMocks
    private AnswerRead answerRead;

    @Mock
    private AnswerPort port;

    @Test
    public void givenAnAnswerCriteriaWhenSearchThenCallPort() {
        var criteria = AnswerCriteria.builder().build();
        answerRead.search(criteria);
        verify(port).search(criteria);
    }
}
