package com.github.paulosalonso.research.adapter.feign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Builder
public class NotificationDTO {
    @NotNull
    private NotificationType type;

    private String sender;

    @Singular
    @NotEmpty
    private List<String> recipients;

    private String subject;

    @NotBlank
    private String message;

    @Singular
    private Map<String, String> additionalProperties;
}
