package com.seuportifolio.controle_cabos.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    // Tem que ser exatamente a MESMA assinatura que usamos para criar o token
    private static final String SECRET = "minha_chave_secreta_super_segura_123";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = recuperarToken(request);

        if (token != null) {
            try {
                // Se tiver token, tentamos ler e conferir a assinatura
                Algorithm algorithm = Algorithm.HMAC256(SECRET);
                DecodedJWT jwt = JWT.require(algorithm)
                        .withIssuer("ControleCabosAPI")
                        .build()
                        .verify(token);

                String username = jwt.getSubject();
                String role = jwt.getClaim("role").asString();

                // Token válido! Avisamos ao sistema de segurança interno que o usuário está liberado
                var authentication = new UsernamePasswordAuthenticationToken(
                        username, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // Se alguém tentar enviar um token falso ou expirado, o segurança barra na hora
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        // Manda o pedido continuar o caminho dele
        filterChain.doFilter(request, response);
    }

    // Pega o token de dentro do cabeçalho da requisição
    private String recuperarToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}