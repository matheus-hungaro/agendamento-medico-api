package com.matheus.agendamento_api.infra;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Agendamento de Consultas API")
                        .description("API REST para gestao de medicos, pacientes e agendamento de consultas medicas.")
                        .version("1.0.0"));
    }
}