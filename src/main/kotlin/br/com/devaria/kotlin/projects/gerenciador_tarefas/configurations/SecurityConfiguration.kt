package br.com.devaria.projetos.gerenciadortarefas.configurations

import br.com.devaria.kotlin.projects.gerenciador_tarefas.repositories.UsuarioRepository
import br.com.devaria.kotlin.projects.gerenciador_tarefas.filters.JWTAuthorizationFilter
import br.com.devaria.kotlin.projects.gerenciador_tarefas.utils.JWTUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
class SecurityConfiguration {
    @Autowired
    private lateinit var jwtUtils : JWTUtils

    @Autowired
    private val usuarioRepository: UsuarioRepository? = null

    @Bean
    fun configureHttpSecurity(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .authorizeHttpRequests { authz: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry ->
                authz
                    .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/usuario").permitAll()
                    .anyRequest().authenticated()
            }
            .cors { it.configurationSource(configuracaoCors()) }
            .addFilter(JWTAuthorizationFilter(http.getSharedObject(AuthenticationManager::class.java), jwtUtils/*, usuarioRepository*/))
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }

        return http.build()
    }

    @Bean
    fun configuracaoCors(): CorsConfigurationSource {
        val configuracao = CorsConfiguration()
        configuracao.addAllowedOriginPattern("*")
        configuracao.addAllowedMethod("*")
        configuracao.addAllowedHeader("*")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuracao)
        return source
    }
}
