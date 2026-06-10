package com.seuportifolio.controle_cabos.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String matricula; // A matrícula usada para o login

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING) // Salva o nome (OPERADOR/SUPERVISOR) no banco em vez de números
    @Column(nullable = false)
    private com.seuportifolio.controle_cabos.model.PerfilAcesso perfil;
}