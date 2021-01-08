# Research

## Sobre o projeto

O Research é uma API pra gerenciamento de pesquisas. É possível cadastrar pesquisas com perguntas múltipla-escolha e 
receber respostas. Posteriomente essas respostas podem ser obtidas para realização de análises.

## Arquitetura

O projeto foi construído utilizando [Clean Arch](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html).

## Qualidade 
![Automated Testing](https://github.com/paulosalonso/research/workflows/Automated%20Testing/badge.svg) ![Mutation Testing](https://github.com/paulosalonso/research/workflows/Mutation%20Testing/badge.svg) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=paulosalonso_research&metric=alert_status)](https://sonarcloud.io/dashboard?id=paulosalonso_research)

A qualidade da aplicação é garantida através dos testes unitários e integrados. Utiliza o [JaCoCo](https://www.jacoco.org/) para validação de cobertura mínima e o [PIT Mutation](https://pitest.org/) para testar mutações de código.
Também é realizada uma análise estática com o [SonarCloud](https://sonarcloud.io/dashboard?id=paulosalonso_research).

## CI

A cada entrega de código (push) os testes são executados e o novo código só é incorporado (merge) a branch master se os testes forem executados com sucesso.
A cada release é criada uma imagem Docker da versão no Docker Hub (paulosalonso/research).

## Banco de dados

A aplicação suporta os bancos H2, MySQL e PostgreSQL. 
Por padrão, utiliza o H2 em memória, os dados ficam totalmente voláteis.
Para utilizar outro banco inicie a aplicação com o profile adequado, criando uma variável de ambiente chamada __spring.profiles.active__ e atribuindo o profile desejado:

- mysql
- postgresql

Exemplo:

> export spring.profiles.active=postgresql

### Configuração de conexão

A aplicação usa as portas padrão de cada banco para se conectar ao localhost, e usa 'research' como nome do banco de dados. No caso do H2 usa uma instância em memória que será descartada ao finalizar a aplicação. Para customizar a URL de conexão crie uma variável de ambiente chamada __spring.datasource.url__ com a string de conexão adequada. Exemplo:

> export spring.datasource.url=jdbc:mysql://192.168.0.100:5432/research_api

## API

O root path da API é __/research/api__

### Documentação

A API é documentada utilizando [OpenAPI](https://swagger.io/specification/) e pode ser acessada via navegador através do path __/research/api/swagger-ui/index.html__

## Execução

Veremos algumas formas para executar a aplicação. Para todas elas é importante observar a cofiguração das variáveis de ambiente citadas anteriormente.

### IDE

Para executar a aplicação na IDE basta importar o projeto e executar a classe com.github.paulosalonso.research.application.ResearchApplication como uma aplicação Java.

### Maven

> mvn spring-boot:run

### java -jar
> mvn clean package \
> java -jar target/research.jar 

### Docker

Existem algumas formas diferentes para rodar a aplicação via Docker, veremos na sequência.
Observação: O docker-compose iniciará também o container do PostgreSQL. O profile __postgresql__ é configurado para a aplicação via variável de ambiente no serviço __research__.

#### Docker Hub
![Docker Hub CI](https://github.com/paulosalonso/research/workflows/Docker%20Hub%20CI/badge.svg)

Para rodar um container Docker da aplicação a partir de uma [imagem do Docker Hub](https://hub.docker.com/repository/docker/paulosalonso/research), acesse o diretório __.docker__ e rode o comando abaixo:

> docker-compose up

#### Build local

##### Maven integrado ao Dockerfile

É possível fazer o build da aplicação e da imagem de uma só vez. Acesse o diretório __.docker__ e rode o comando abaixo:

> docker-compose -f docker-compose.yml -f docker-compose.local-build.yml up --build

Todas as dependências Maven necessárias serão baixadas, ainda que estejam no seu repositório local, pois o Docker utilizará um repositório exclusivo para o build.

##### Maven manual

Para evitar o download de todas as dependências a cada build, é possível fazer o build do Maven manualmente pra que seja utilizado o seu repositório local. Acesse o diretório raiz da aplicação e rode o comando abaixo:

> mvn clean package -Pdocker-local -Pno-tests

Depois acesse o diretório __.docker__ e rode o comando abaixo:

> docker-compose -f docker-compose.yml -f docker-compose.local-pre-build.yml up --build

## Observabilidade

### Métricas

A aplicação utiliza do [Spring Actuator](https://docs.spring.io/spring-boot/docs/current/actuator-api/htmlsingle/) para expor dados sobre sua execução.

### Logs

Os logs são gerenciados pelo [SLF4J](http://www.slf4j.org/), e utiliza o [Logback](http://logback.qos.ch/) como implementação.

### Dashboard

O docker-compose existente no projeto inclui o [Prometheus](https://prometheus.io/) e o [Grafana](https://grafana.com/).
O Grafana é exposto na porta 3000 com usuário __admin__ e senha __123456__. Ao logar, será exibido um dashboard preconfigurado que consome os dados fornecidos pelo Actuator ao Prometheus.
