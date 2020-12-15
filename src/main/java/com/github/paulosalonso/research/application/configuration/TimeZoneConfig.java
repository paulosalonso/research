package com.github.paulosalonso.research.application.configuration;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Configuration
public class TimeZoneConfig {

    @PostConstruct
    public void configure() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
