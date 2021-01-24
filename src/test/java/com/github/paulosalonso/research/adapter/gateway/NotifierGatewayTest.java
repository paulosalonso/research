package com.github.paulosalonso.research.adapter.gateway;

import com.github.paulosalonso.research.adapter.feign.NotificationDTO;
import com.github.paulosalonso.research.adapter.feign.NotifierClient;
import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.domain.Option;
import com.github.paulosalonso.research.domain.Question;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.github.paulosalonso.research.adapter.feign.NotificationType.EMAIL;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotifierGatewayTest {

    @InjectMocks
    private NotifierGateway gateway;

    @Mock
    private NotifierClient notifierClient;

    @Mock
    private QuestionGateway questionGateway;

    @Mock
    private OptionGateway optionGateway;

    @Test
    public void givenAAnswerWhenNotifyThenCallNotifierClient() {
        var answer = Answer.builder()
                .date(OffsetDateTime.now())
                .researchId(UUID.randomUUID())
                .questionId(UUID.randomUUID())
                .optionId(UUID.randomUUID())
                .build();

        when(questionGateway.read(answer.getResearchId(), answer.getQuestionId())).thenReturn(Question.builder()
                .description("question description")
                .multiSelect(false)
                .build());
        when(optionGateway.read(answer.getQuestionId(), answer.getOptionId())).thenReturn(Option.builder()
                .description("option description")
                .build());

        gateway.notifyAnswer(answer);

        var notificationCaptor = ArgumentCaptor.forClass(NotificationDTO.class);

        verify(notifierClient).notify(notificationCaptor.capture());

        var notification = notificationCaptor.getValue();
        assertThat(notification.getType()).isEqualTo(EMAIL);
        assertThat(notification.getSender()).isEqualTo("Research <noreply@research.com>");
        assertThat(notification.getRecipients())
                .hasSize(1)
                .first()
                .satisfies(recipient -> assertThat(recipient).isEqualTo("paulo_alonso_@hotmail.com"));
        assertThat(notification.getSubject()).isEqualTo("Research Notification");
        assertThat(notification.getMessage()).isEqualTo(String.format(
                "Answer received at %s\n\nQuestion: question description\nSelected option: option description",
                ISO_OFFSET_DATE_TIME.format(answer.getDate())));
    }
}
