package com.github.paulosalonso.research.application.apidoc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

import static java.util.function.Predicate.not;

@Configuration
@EnableWebMvc
@EnableOpenApi
public class OpenApiConfiguration implements WebMvcConfigurer {

    @Bean
    public Docket docket() {
        Docket docket = new Docket(DocumentationType.OAS_30)
                .ignoredParameterTypes(ServletWebRequest.class)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any()
                        .and(not(PathSelectors.ant("/research/api/error")))
                        .and(not(PathSelectors.ant("/research/api/actuator/**"))))
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(buildApiInfo())
                .securitySchemes(List.of(apiKey()))
                .securityContexts(List.of(buildSecurityContext()));

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

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private SecurityContext buildSecurityContext() {
        return SecurityContext.builder()
                .securityReferences(buildSecurityReference())
                .operationSelector(operationContext -> true)
                .build();
    }

    private List<SecurityReference> buildSecurityReference() {
        AuthorizationScope authorizationScope = new AuthorizationScope("*", "Full access");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return List.of(new SecurityReference("Authorization", authorizationScopes));
    }

}
