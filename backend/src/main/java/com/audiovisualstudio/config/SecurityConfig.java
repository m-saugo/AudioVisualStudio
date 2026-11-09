
package com.audiovisualstudio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        HttpSessionSecurityContextRepository contextRepository =
                new HttpSessionSecurityContextRepository();

        http
                // Protecao CSRF para POST, PUT e DELETE
                .csrf(csrf -> csrf
                        .csrfTokenRepository(
                                new HttpSessionCsrfTokenRepository()
                        )
                )

                // Guarda a autenticacao na sessao
                .securityContext(context -> context
                        .securityContextRepository(contextRepository)
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.IF_REQUIRED
                        )
                )

                // Permissoes das rotas
                .authorizeHttpRequests(auth -> auth

                        // Permitir carregar a interface React
                        .requestMatchers(
                                HttpMethod.GET,
                                "/",
                                "/index.html",
                                "/assets/**",
                                "/favicon.ico"
                        ).permitAll()

                        // Obter token CSRF
                        .requestMatchers(
                                HttpMethod.GET,
                                "/csrf"
                        ).permitAll()

                        // Cadastro publico
                        .requestMatchers(
                                HttpMethod.POST,
                                "/cadastro"
                        ).permitAll()

                        // Login publico
                        .requestMatchers(
                                HttpMethod.POST,
                                "/login"
                        ).permitAll()

                        // Logout
                        .requestMatchers(
                                HttpMethod.POST,
                                "/login/sair"
                        ).authenticated()

                        // Consultar e editar a propria conta
                        .requestMatchers(
                                "/minha-conta",
                                "/minha-conta/**"
                        ).authenticated()

                        // Profissionais podem consultar seus filmes
                        .requestMatchers(
                                HttpMethod.GET,
                                "/filmes/por-ator/**",
                                "/filmes/por-diretor/**",
                                "/filmes/por-roteirista/**"
                        ).authenticated()

                        // Demais funcionalidades:
                        // somente administrador
                        .anyRequest().hasRole("ADMINISTRADOR")
                )

                // Retorna 401 quando nao estiver autenticado
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(
                                (request, response, erro) ->
                                        response.sendError(401)
                        )
                )

                // Desativa os formularios padrao do Spring
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
