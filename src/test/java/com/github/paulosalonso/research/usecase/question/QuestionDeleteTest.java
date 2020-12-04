package com.github.paulosalonso.research.usecase.question;

import com.github.paulosalonso.research.usecase.port.QuestionPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class QuestionDeleteTest {

    @InjectMocks
    private QuestionDelete questionDelete;

    @Mock
    private QuestionPort port;

    @Test
    public void givenAResearchIdAndAQuestionIdWhenDeleteThenCallPort() {
        var researchId = UUID.randomUUID();
        var questionId = UUID.randomUUID();

        questionDelete.delete(researchId, questionId);

        verify(port).delete(researchId, questionId);
    }
}
