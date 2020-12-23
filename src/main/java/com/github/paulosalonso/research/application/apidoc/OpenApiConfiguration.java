package com.github.paulosalonso.research.application.apidoc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.function.Predicate;

@Configuration
@EnableWebMvc
@EnableSwagger2
public class OpenApiConfiguration implements WebMvcConfigurer {

    @Bean
    public Docket docket() {
        Docket docket = new Docket(DocumentationType.OAS_30)
                .ignoredParameterTypes(ServletWebRequest.class)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any().and(Predicate.not(PathSelectors.ant("/research/api/error"))))
                .build()
                .apiInfo(buildApiInfo());

        addTags(docket);

        return docket;
    }

    private void addTags(Docket docket) {
        docket
                .tags(new Tag("Researches", "Researches Operations", 1))
                .tags(new Tag("Questions", "Questions Operations", 2))
                .tags(new Tag("Options", "Options Operations", 3))
                .tags(new Tag("Answers", "Answers Operations"));
    }

    private ApiInfo buildApiInfo() {
        return new ApiInfoBuilder()
                .title("Research API")
                .description("O Research é uma API pra gerenciamento de pesquisas. É possível cadastrar pesquisas com perguntas múltipla-escolha e receber respostas. Posteriomente essas respostas podem ser obtidas para realização de análises.")
                .version("0.0.1")
                .contact(new Contact("Paulo Alonso",
                        "https://www.linkedin.com/in/paulo-alonso-67b082149/", "paulo_alonso_@hotmail.com"))
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("index.html")
                .addResourceLocations("classpath:/META-INF/resources/");
    }

}
