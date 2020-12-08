package com.github.paulosalonso.research.application.configuration;

import com.github.paulosalonso.research.usecase.port.ResearchPort;
import com.github.paulosalonso.research.usecase.research.ResearchCreate;
import com.github.paulosalonso.research.usecase.research.ResearchDelete;
import com.github.paulosalonso.research.usecase.research.ResearchRead;
import com.github.paulosalonso.research.usecase.research.ResearchUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class ResearchConfig {

    private final ResearchPort port;

    @Bean
    public ResearchCreate researchCreate() {
        return new ResearchCreate(port);
    }

    @Bean
    public ResearchRead researchRead() {
        return new ResearchRead(port);
    }

    @Bean
    public ResearchUpdate researchUpdate() {
        return new ResearchUpdate(port);
    }

    @Bean
    public ResearchDelete researchDelete() {
        return new ResearchDelete(port);
    }
}
