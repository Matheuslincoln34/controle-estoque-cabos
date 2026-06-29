package com.seuportifolio.controle_cabos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Quando alguém entrar na raiz do site ("/"), é jogado para o Swagger
    @GetMapping("/")
    public String home() {
        return "redirect:/swagger-ui/index.html";
    }
}