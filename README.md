# Pós-Tech Arquitetura e Desenvolvimento Java
- Fase 5: Neste desafio, o objetivo era desenvolver um sistema de e-commerce que permitisse aos usuários realizassem as seguintes operações:
1. Login e registro de usuários utilizando as ferramentas do Spring Security, para garantir autenticação e autorização eficientes no sistema.
2. Getão de itens, acessível apenas para usuários administradores, com o objetivo de permitir o controle completo do cadastro e manutenção de itens, incluindo seus preços.
3. Funcionalidade de carrinho de compras, que permitisse aos usuários adicionar e remover itens.
4. Simulação de processo de pagamento, oferecendo aos usuários a oportunidade de visualizar os itens presentes no carrinho e concluir uma compra fictícia.


## Índice

- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Uso](#uso)
- [Relatório técnico](#relatório-técnico)

## Pré-requisitos
Para rodar o projeto na sua máquina é necessário:
- Java 17
- Gradlew
- Docker

## Instalação
Siga as etapas abaixo para configurar e executar o projeto em seu ambiente local:
1. Clone o repositório
   ```sh
   git clone https://github.com/mayaravlima/tech-challenge-4
   ```  
2. Navegue até o diretório do projeto:
   ```sh
   cd tech-challenge-5
   ```
3. Rode o comando para utilizar o Docker Compose e subir o banco de dados:
   ```sh
    docker-compose up --force-recreate -d --build
    ```
4. Acesse os endpoints
   ```sh
   localhost:8888/
   ```
## Uso
Foi utilizado o Postman para documentar a API. Para acessar a documentação basta acessar o endpoint:

```sh
   https://documenter.getpostman.com/view/11575875/2sA35D53Jo 
```

## Relatório Técnico
### Estrutura do projeto:

### Decisões de arquitetura
