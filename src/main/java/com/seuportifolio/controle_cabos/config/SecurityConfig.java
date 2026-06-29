package com.seuportifolio.controle_cabos.config;

import com.seuportifolio.controle_cabos.security.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                // Desliga o sistema antigo e transforma a API em Stateless (exige token)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/").permitAll()
                        // Libera a porta da recepção para as pessoas poderem pegar o token
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/api/estoque/estorno", "/api/estoque/abastecimento").hasRole("SUPERVISOR")
                        .anyRequest().authenticated()
                )
                // Coloca o nosso novo segurança (Filtro) na porta da frente
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // Bean necessário para o AuthController conseguir conferir a senha
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails chefe = User.withDefaultPasswordEncoder()
                .username("chefe")
                .password("admin123")
                .roles("SUPERVISOR")
                .build();

        UserDetails equipeRua = User.withDefaultPasswordEncoder()
                .username("equipe_rua")
                .password("senha123")
                .roles("EQUIPE")
                .build();

        return new InMemoryUserDetailsManager(chefe, equipeRua);
    }
}