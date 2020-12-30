package com.github.paulosalonso.research.application.configuration;

import com.github.paulosalonso.research.usecase.port.QuestionPort;
import com.github.paulosalonso.research.usecase.port.ResearchPort;
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

    private final QuestionPort questionPort;
    private final ResearchPort researchPort;

    @Bean
    public QuestionCreate questionCreate() {
        return new QuestionCreate(questionPort, researchPort);
    }

    @Bean
    public QuestionRead questionRead() {
        return new QuestionRead(questionPort);
    }

    @Bean
    public QuestionUpdate questionUpdate() {
        return new QuestionUpdate(questionPort);
    }

    @Bean
    public QuestionDelete questionDelete() {
        return new QuestionDelete(questionPort);
    }
}
