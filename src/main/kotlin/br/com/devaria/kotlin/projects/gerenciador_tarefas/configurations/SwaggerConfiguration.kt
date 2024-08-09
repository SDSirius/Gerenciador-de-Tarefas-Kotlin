package br.com.devaria.kotlin.projects.gerenciador_tarefas.configurations

import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration

class OpenApiConfig {

    @Bean
    fun publicApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("public")
            .pathsToMatch("/**")  // Inclui todas as rotas começando com /api
            .build()
    }
}