package com.github.paulosalonso.research.application.configuration;

import com.github.paulosalonso.research.usecase.answer.AnswerCreate;
import com.github.paulosalonso.research.usecase.answer.AnswerRead;
import com.github.paulosalonso.research.usecase.answer.AnswerValidator;
import com.github.paulosalonso.research.usecase.port.AnswerPort;
import com.github.paulosalonso.research.usecase.port.OptionPort;
import com.github.paulosalonso.research.usecase.port.QuestionPort;
import com.github.paulosalonso.research.usecase.port.ResearchPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class AnswerConfig {

    private final AnswerPort answerPort;

    @Bean
    public AnswerCreate answerCreate(ResearchPort researchPort, QuestionPort questionPort, OptionPort optionPort) {
        return new AnswerCreate(answerPort, new AnswerValidator(researchPort, questionPort, optionPort));
    }

    @Bean
    public AnswerRead answerRead() {
        return new AnswerRead(answerPort);
    }
}
