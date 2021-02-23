package com.github.paulosalonso.research.usecase.answer;

import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.usecase.exception.InvalidAnswerException;
import com.github.paulosalonso.research.usecase.port.AnswerPort;
import com.github.paulosalonso.research.usecase.port.NotifierPort;
import com.github.paulosalonso.research.usecase.port.OptionPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnswerCreateTest {

    private static final String TENANT = "tenant";

    @InjectMocks
    private AnswerCreate answerCreate;

    @Mock
    private AnswerPort answerPort;

    @Mock
    private AnswerValidator validator;

    @Mock
    private OptionPort optionPort;

    @Mock
    private NotifierPort notifierPort;

    @Test
    public void givenAnAnswerWhenCreateThenCallPort() {
        var testInit = OffsetDateTime.now();

        var toSave = Answer.builder()
                .researchId(UUID.randomUUID())
                .questionId(UUID.randomUUID())
                .optionId(UUID.randomUUID())
                .build();

        when(optionPort.shouldNotify(toSave.getOptionId())).thenReturn(false);

        answerCreate.create(toSave.getResearchId(), TENANT, List.of(toSave));

        ArgumentCaptor<Answer> answerCaptor = ArgumentCaptor.forClass(Answer.class);
        verify(answerPort).create(answerCaptor.capture());

        var saved = answerCaptor.getValue();
        assertThat(saved.getDate()).isBetween(testInit, OffsetDateTime.now());
        assertThat(saved.getResearchId()).isEqualTo(toSave.getResearchId());
        assertThat(saved.getQuestionId()).isEqualTo(toSave.getQuestionId());
        assertThat(saved.getOptionId()).isEqualTo(toSave.getOptionId());

        verify(validator).validate(toSave.getResearchId(), TENANT, List.of(toSave));
        verify(optionPort).shouldNotify(toSave.getOptionId());
        verifyNoInteractions(notifierPort);
    }

    @Test
    public void givenAnAnswerWhenIndividualValidationThrowsExceptionThenRethrowsIt() {
        var answer = Answer.builder()
                .researchId(UUID.randomUUID())
                .build();

        var exception = new InvalidAnswerException("test exception");

        doThrow(exception).when(validator).validate(answer.getResearchId(), TENANT, List.of(answer));

        assertThatThrownBy(() -> answerCreate.create(answer.getResearchId(), TENANT, List.of(answer)))
                .isSameAs(exception);

        verify(validator).validate(answer.getResearchId(), TENANT, List.of(answer));
        verifyNoInteractions(optionPort);
        verifyNoInteractions(notifierPort);
    }

    @Test
    public void givenAnAnswerContainingNotifyOptionWhenCreateThenCallNotifierPort() {
        var answer = Answer.builder()
                .researchId(UUID.randomUUID())
                .questionId(UUID.randomUUID())
                .optionId(UUID.randomUUID())
                .build();

        when(optionPort.shouldNotify(answer.getOptionId())).thenReturn(true);

        answerCreate.create(answer.getResearchId(), TENANT, List.of(answer));

        verify(optionPort).shouldNotify(answer.getOptionId());
        verify(notifierPort).notifyAnswer(answer);
    }
}
