package com.github.paulosalonso.research.adapter.feign;

import feign.Feign;
import feign.jackson.JacksonEncoder;
import feign.mock.HttpMethod;
import feign.mock.MockClient;
import feign.mock.RequestKey;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThatCode;

public class NotifierClientTest {

    private static final String BASE_URL = "http://api.notifier.com";

    @Test
    public void whenSendNotifierThenReturnResponse() {
        var body = NotificationDTO.builder()
                .type(NotificationType.EMAIL)
                .sender("sender")
                .recipient("recipient")
                .subject("subject")
                .message("message")
                .build();

        var client = mockFeignClient(new MockClient().add(RequestKey
                .builder(HttpMethod.POST, BASE_URL.concat("/notifications"))
                .build(), HttpStatus.NO_CONTENT.value()));

        assertThatCode(() -> client.notify(body))
                .doesNotThrowAnyException();
    }

    private NotifierClient mockFeignClient(MockClient mockClient) {
        return Feign.builder()
                .client(mockClient)
                .encoder(new JacksonEncoder())
                .contract(new SpringMvcContract())
                .target(NotifierClient.class, BASE_URL);
    }
}
