package com.github.paulosalonso.research.application.configuration;

import com.github.paulosalonso.research.usecase.option.OptionCreate;
import com.github.paulosalonso.research.usecase.option.OptionDelete;
import com.github.paulosalonso.research.usecase.option.OptionRead;
import com.github.paulosalonso.research.usecase.option.OptionUpdate;
import com.github.paulosalonso.research.usecase.port.OptionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class OptionConfig {

    private final OptionPort port;

    @Bean
    public OptionCreate optionCreate() {
        return new OptionCreate(port);
    }

    @Bean
    public OptionRead optionRead() {
        return new OptionRead(port);
    }

    @Bean
    public OptionUpdate optionUpdate() {
        return new OptionUpdate(port);
    }

    @Bean
    public OptionDelete optionDelete() {
        return new OptionDelete(port);
    }
}
