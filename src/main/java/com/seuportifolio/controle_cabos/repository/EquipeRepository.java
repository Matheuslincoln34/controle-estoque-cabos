package com.seuportifolio.controle_cabos.repository;

import com.seuportifolio.controle_cabos.model.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Long> {
    // Só com essa linha, o Spring já nos dá os métodos de salvar, deletar, buscar por ID, etc.
}