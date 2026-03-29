package fr.esgi.filmographie.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI filmographieOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Filmographie API")
                        .description("API REST pour la gestion des films, castings, roles, genres et personnes")
                        .version("v1")
                        .contact(new Contact().name("Equipe Filmographie"))
                        .license(new License().name("Proprietary"))
                );
    }
}

