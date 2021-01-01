package com.github.paulosalonso.research.application.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        converters.stream()
                .filter(converter -> converter instanceof MappingJackson2HttpMessageConverter)
                .findFirst()
                .ifPresent(converters::remove);

        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Springfox enables this feature
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        builder.serializationInclusion(JsonInclude.Include.NON_EMPTY);

        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));

    }

}