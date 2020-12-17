package com.github.paulosalonso.research.application.configuration;

import com.github.paulosalonso.research.usecase.port.QuestionPort;
import com.github.paulosalonso.research.usecase.question.QuestionCreate;
import com.github.paulosalonso.research.usecase.question.QuestionDelete;
import com.github.paulosalonso.research.usecase.question.QuestionRead;
import com.github.paulosalonso.research.usecase.question.QuestionUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class QuestionConfig {

    private final QuestionPort port;

    @Bean
    public QuestionCreate questionCreate() {
        return new QuestionCreate(port);
    }

    @Bean
    public QuestionRead questionRead() {
        return new QuestionRead(port);
    }

    @Bean
    public QuestionUpdate questionUpdate() {
        return new QuestionUpdate(port);
    }

    @Bean
    public QuestionDelete questionDelete() {
        return new QuestionDelete(port);
    }
}
