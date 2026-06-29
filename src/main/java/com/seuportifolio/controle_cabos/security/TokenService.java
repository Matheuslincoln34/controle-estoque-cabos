package com.seuportifolio.controle_cabos.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // A assinatura do seu hotel. Se alguém tentar falsificar um token, o sistema barra porque não tem essa "senha" interna.
    // Em sistemas reais de produção, essa senha fica escondida nas variáveis do servidor (Render).
    private static final String SECRET = "minha_chave_secreta_super_segura_123";

    // Método que fabrica o cartão magnético
    public String gerarToken(String username, String role) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);

        return JWT.create()
                .withIssuer("ControleCabosAPI") // Nome da sua empresa/sistema
                .withSubject(username) // Nome de quem está logando
                .withClaim("role", role) // Cargo (SUPERVISOR ou EQUIPE)
                .withExpiresAt(gerarDataExpiracao()) // Validade do cartão
                .sign(algorithm); // Carimba com a assinatura secreta
    }

    // Regra de negócio: O token dura exatamente 2 horas a partir do momento do login
    private Instant gerarDataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}