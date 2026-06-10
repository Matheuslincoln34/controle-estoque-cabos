# Sistema de Controle de Estoque e Logística de Campo ⚡

Este é um sistema de back-end robusto desenvolvido em Java e Spring Boot, projetado para resolver um dos desafios mais críticos na logística de concessionárias de serviços públicos e infraestrutura de rede: a **rastreabilidade e prestação de contas de materiais em operações de campo**.

Ao contrário de sistemas genéricos que associam o estoque a placas de veículos, esta solução modela a realidade operacional real, atrelando a carga de materiais (como cabos e ramais) diretamente à matrícula do **Líder de Turma (Encarregado Operacional - BTC-2)**, blindando a operação contra perdas financeiras e falhas de auditoria.

## 🚀 Funcionalidades Principais

* **Rastreabilidade por Matrícula Operacional (BTC-2):** Vinculação estrita do consumo e saldo de materiais à responsabilidade do líder da equipe na rua, garantindo auditorias seguras independentemente da rotatividade da frota.
* **Livro-Razão Imutável (Logística à Prova de Falhas):** O sistema proíbe a exclusão física de registros de consumo. Em caso de erros humanos de digitação (ex: lançar 450m em vez de 45m), o software exige um fluxo de **Estorno/Ajuste Justificado**, gerando um lançamento compensatório de entrada amarrado ao ID original para rastreabilidade completa.
* **Segurança e Hierarquia de Perfis (RBAC):** Proteção de rotas com Spring Security. Equipes de campo possuem acesso restrito para lançamentos de consumo (`ROLE_EQUIPE`), enquanto funções críticas de ajuste e estorno exigem a credencial de supervisão (`ROLE_SUPERVISOR`).
* **Cálculo de Saldo Dinâmico para Auditoria:** Algoritmo otimizado direto no banco de dados através de consultas SQL customizadas (`CASE WHEN`), consolidando saídas e devoluções em tempo real.
* **Documentação Interativa (OpenAPI/Swagger):** Documentação completa das rotas expostas, permitindo testes rápidos e integração simplificada com aplicações front-end ou mobile.

## 🛠️ Tecnologias Utilizadas

* **Java 21** & **Spring Boot 3**
* **Spring Security** (Autenticação e controle de acesso baseado em regras)
* **Spring Data JPA** & **Hibernate** (Persistência de dados)
* **H2 Database** (Banco de dados relacional em memória para ciclos ágeis de teste)
* **Project Lombok** (Escrita de código limpo)
* **Springdoc OpenAPI / Swagger UI** (Documentação automatizada da API)
* **Maven** (Gerenciamento de dependências)

## 📐 Estrutura do Banco de Dados

O modelo de dados foi estruturado para refletir com fidelidade a rotina de campo:

* `Equipe`: Armazena a composição da dupla (Líder BTC-2 e Ajudante BTC-1), capturando dados de identificação e matrículas de auditoria.
* `Ocorrencia`: Registra o evento de campo associado ao número da Ordem de Serviço (OS) e ao tipo de atendimento (ex: Substituição de Ramal).
* `MovimentacaoEstoque`: O coração logístico do sistema, funcionando como um livro caixa onde cada registro é classificado como `SAIDA` (consumo) ou `ENTRADA` (estorno/devolução), contendo campos para justificativas e chaves estrangeiras autorreferenciais para rastreamento de correções.

## 📋 Como Executar e Testar o Projeto

### Pré-requisitos
* Java JDK 17 ou superior instalado.
* Maven instalado (ou uso do wrapper embutido).

### Inicialização
1. Clone o repositório para sua máquina local.
2. Execute a aplicação através da sua IDE de preferência ou via terminal:
   ```bash
   mvn spring-boot:run