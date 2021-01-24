package com.github.paulosalonso.research.adapter.gateway;

import com.github.paulosalonso.research.adapter.feign.NotificationDTO;
import com.github.paulosalonso.research.adapter.feign.NotifierClient;
import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.usecase.port.NotifierPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.github.paulosalonso.research.adapter.feign.NotificationType.EMAIL;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

@RequiredArgsConstructor
@Component
public class NotifierGateway implements NotifierPort {

    private final NotifierClient notifierClient;
    private final QuestionGateway questionGateway;
    private final OptionGateway optionGateway;

    @Override
    public void notifyAnswer(Answer answer) {
        notifierClient.notify(buildNotification(answer));
    }

    private NotificationDTO buildNotification(Answer answer) {
        return NotificationDTO.builder()
                .type(EMAIL)
                .sender("Research <noreply@research.com>")
                .recipient("paulo_alonso_@hotmail.com")
                .subject("Research Notification")
                .message(String.format("Answer received at %s\n\nQuestion: %s\nSelected option: %s",
                        ISO_OFFSET_DATE_TIME.format(answer.getDate()),
                        questionGateway.read(answer.getResearchId(), answer.getQuestionId()).getDescription(),
                        optionGateway.read(answer.getQuestionId(), answer.getOptionId()).getDescription()))
                .build();
    }
}
