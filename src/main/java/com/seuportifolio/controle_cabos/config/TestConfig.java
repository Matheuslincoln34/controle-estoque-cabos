package com.seuportifolio.controle_cabos.config;

import com.seuportifolio.controle_cabos.model.CargoOperacional;
import com.seuportifolio.controle_cabos.model.Equipe;
import com.seuportifolio.controle_cabos.repository.EquipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig implements CommandLineRunner {

    @Autowired
    private EquipeRepository equipeRepository;

    @Override
    public void run(String... args) throws Exception {
        // Se o banco estiver vazio, cria o Walmir e o Wanderson
        if (equipeRepository.count() == 0) {
            Equipe equipe = new Equipe();
            equipe.setNomeEquipe("Walmir x Wanderson");

            // Atrelando a responsabilidade da carga ao Líder (BTC-2)
            equipe.setNomeLider("Walmir");
            equipe.setMatriculaLider("MAT-998877");
            equipe.setCargoLider(CargoOperacional.BTC_2);

            // Dados do Ajudante (BTC-1)
            equipe.setNomeAjudante("Wanderson");
            equipe.setCargoAjudante(CargoOperacional.BTC_1);

            equipe.setAtivo(true);

            equipeRepository.save(equipe);
            System.out.println("✅ Equipe Walmir x Wanderson (Líder BTC-2) criada com sucesso no banco de dados!");
        }
    }
}