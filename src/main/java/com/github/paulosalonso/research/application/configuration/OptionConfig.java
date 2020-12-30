package com.github.paulosalonso.research.application.configuration;

import com.github.paulosalonso.research.usecase.option.OptionCreate;
import com.github.paulosalonso.research.usecase.option.OptionDelete;
import com.github.paulosalonso.research.usecase.option.OptionRead;
import com.github.paulosalonso.research.usecase.option.OptionUpdate;
import com.github.paulosalonso.research.usecase.port.OptionPort;
import com.github.paulosalonso.research.usecase.port.QuestionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class OptionConfig {

    private final OptionPort optionPort;
    private final QuestionPort questionPort;

    @Bean
    public OptionCreate optionCreate() {
        return new OptionCreate(optionPort, questionPort);
    }

    @Bean
    public OptionRead optionRead() {
        return new OptionRead(optionPort);
    }

    @Bean
    public OptionUpdate optionUpdate() {
        return new OptionUpdate(optionPort);
    }

    @Bean
    public OptionDelete optionDelete() {
        return new OptionDelete(optionPort);
    }
}
