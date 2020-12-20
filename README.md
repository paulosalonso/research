# Research

## Sobre o projeto

O Research é uma API pra gerenciamento de pesquisas. É possível cadastrar pesquisas com perguntas múltipla-escolha e 
receber respostas. Posteriomente essas respostas podem ser obtidas para realização de análises.

## Arquitetura

O projeto foi construído utilizando [Clean Arch](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html).

## Qualidade 
![Automated Testing](https://github.com/paulosalonso/research/workflows/Automated%20Testing/badge.svg) ![Mutation Testing](https://github.com/paulosalonso/research/workflows/Mutation%20Testing/badge.svg) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=paulosalonso_research&metric=alert_status)](https://sonarcloud.io/dashboard?id=paulosalonso_research)

A qualidade da aplicação é garantida através dos testes unitários e integrados, com validação de cobertura mínima.
Também é realizada uma análise estática de código com o [SonarCloud](https://sonarcloud.io/dashboard?id=paulosalonso_research).

## CI

A cada entrega de código (push) os testes são executos e o novo código só é incorporado (merge) a branch master se os
testes forem executados com sucesso.
