package com.seuportifolio.controle_cabos.controller;

import com.seuportifolio.controle_cabos.dto.LoginDTO;
import com.seuportifolio.controle_cabos.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    // A rota pública onde o usuário bate para tentar entrar
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO dto) {

        // 1. O Spring Security confere se o login e a senha estão corretos
        var credenciais = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        var authentication = authenticationManager.authenticate(credenciais);

        // 2. Se a senha estiver certa, pegamos o cargo (Role) do usuário
        String role = authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

        // 3. Mandamos a nossa máquina imprimir o Token JWT carimbado
        String token = tokenService.gerarToken(authentication.getName(), role);

        // 4. Entregamos o "cartão magnético" para o usuário
        return ResponseEntity.ok(token);
    }
}