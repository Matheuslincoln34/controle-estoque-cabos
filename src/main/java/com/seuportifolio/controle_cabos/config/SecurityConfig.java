package com.seuportifolio.controle_cabos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // 1. Libera o acesso público apenas para visualizar a documentação do Swagger
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/").permitAll()

                        // 2. A Sala do Cofre: APENAS o perfil SUPERVISOR...
                        .requestMatchers("/api/estoque/estorno", "/api/estoque/abastecimento").hasRole("SUPERVISOR")

                        // 3. Qualquer outra rota exige autenticação
                        .anyRequest().authenticated()
                )

                // Liga a leitura de senhas no padrão "Basic Auth" (Ideal para testes em APIs)
                .httpBasic(withDefaults())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }

    // Gerando os crachás da empresa na memória do sistema
    @Bean
    public UserDetailsService userDetailsService() {

        // Crachá 1: A equipe de campo (ex: Walmir e Wanderson)
        UserDetails equipe = User.builder()
                .username("equipe_rua")
                .password("{noop}senha123") // O {noop} diz ao Java para não exigir criptografia avançada neste teste
                .roles("EQUIPE")
                .build();

        // Crachá 2: A supervisão do almoxarifado
        UserDetails supervisor = User.builder()
                .username("chefe")
                .password("{noop}admin123")
                .roles("SUPERVISOR", "EQUIPE") // O chefe recebe as duas credenciais
                .build();

        return new InMemoryUserDetailsManager(equipe, supervisor);
    }
}