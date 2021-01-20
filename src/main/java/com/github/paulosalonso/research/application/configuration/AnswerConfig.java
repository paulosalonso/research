package com.github.paulosalonso.research.application.configuration;

import com.github.paulosalonso.research.usecase.answer.AnswerCreate;
import com.github.paulosalonso.research.usecase.answer.AnswerRead;
import com.github.paulosalonso.research.usecase.answer.AnswerValidator;
import com.github.paulosalonso.research.usecase.port.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class AnswerConfig {

    private final AnswerPort answerPort;
    private final NotifierPort notifierPort;

    @Bean
    public AnswerCreate answerCreate(ResearchPort researchPort, QuestionPort questionPort, OptionPort optionPort) {
        return new AnswerCreate(answerPort, new AnswerValidator(researchPort, questionPort, optionPort), optionPort, notifierPort);
    }

    @Bean
    public AnswerRead answerRead() {
        return new AnswerRead(answerPort);
    }
}
