package com.github.paulosalonso.research.adapter.gateway;

import com.github.paulosalonso.research.domain.Answer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;

@ExtendWith(MockitoExtension.class)
public class NotifierGatewayTest {

    @InjectMocks
    private NotifierGateway gateway;

    @Test
    public void whenNotify() {
        assertThatCode(() -> gateway.notifyAnswer(Answer.builder().build()))
                .doesNotThrowAnyException();
    }
}
