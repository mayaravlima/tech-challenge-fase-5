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
3. Rode o comando para utilizar o Docker Compose e subir os bancos de dados:
   ```sh
    docker-compose up --force-recreate -d --build
    ```
4. Execute cada um dos microserviços (de preferência começar com o ecommerce-registry):
   ```sh
   ./gradlew bootRun 
5. Acesse os endpoints
   ```sh
   localhost:8888/
   ```
## Uso
Foi utilizado o Postman para documentar a API. Para acessar a documentação basta acessar o endpoint:

```sh
   https://documenter.getpostman.com/view/11575875/2sA35D53Jo 
```

## Relatório Técnico
### Tecnologias utilizadas
1. Spring Boot Security: O Spring Boot Security foi escolhido para fornecer recursos robustos de autenticação e autorização para nosso aplicativo. Ele oferece uma configuração flexível e fácil de usar para proteger endpoints, controlar o acesso do usuário e implementar práticas recomendadas de segurança.
2. Spring Cloud Starter Netflix Eureka: O Spring Cloud Netflix Eureka foi selecionado para facilitar o registro e a descoberta de serviços em nosso ambiente de microserviços. Ele fornece um serviço de descoberta de serviço, permitindo que nossos serviços se comuniquem dinamicamente sem a necessidade de configuração manual de endereços de IP e portas.
3. Spring Boot Starter Validation: O Spring Boot Starter Validation foi integrado ao nosso projeto para simplificar a validação de dados de entrada. Ele oferece um conjunto de anotações simples e poderosas que podem ser aplicadas aos campos de nossos modelos para garantir a integridade e a consistência dos dados recebidos pela nossa aplicação.
4. Lombok: O Lombok foi adotado para reduzir a verbosidade do código e aumentar a produtividade do desenvolvedor. Ele nos permite gerar automaticamente getters, setters, construtores e outros métodos comuns, eliminando a necessidade de escrever código repetitivo e boilerplate.
5. jsonwebtoken:jjwt-impl e jsonwebtoken:jjwt-jackson: Estas bibliotecas foram escolhidas para oferecer suporte à geração e validação de tokens JWT (JSON Web Tokens) em nosso sistema. Os tokens JWT são uma forma segura de transmitir informações de forma assinada entre partes e são amplamente utilizados para autenticação e autorização em aplicações modernas.

### Decisões de arquitetura
Foram desenvolvidos quatro microserviços distintos para compor a infraestrutura:

1. auth-api: Responsável pelo gerenciamento de registros e autenticação de usuários.
2. product-api: Encarregado do gerenciamento de produtos.
3. cart-api: Responsável pelo gerenciamento do carrinho de compras.
4. payment-api: Encarregado do gerenciamento de pagamentos de pedidos.

Para lidar com as chamadas entre esses microserviços, foram criados o ecommerce-registry e o gateway, que funcionam como componentes de roteamento e descoberta de serviços.

A plataforma diferencia dois tipos de usuários: ADMIN e CUSTOMER. Caso não seja especificada a função (role) de um usuário durante sua criação, ele será automaticamente classificado como CUSTOMER, garantindo assim uma gestão adequada dos privilégios de acesso.

Durante o processo de login, o usuário fornece seu email e senha, e um token de autenticação é gerado. Esse token deve ser incluído em todas as requisições subsequentes, utilizando a autorização do tipo Bearer Token.

Ao efetuar login, um cart_id é gerado para a sessão do usuário e enviado pelo cabeçalho via gateway. É importante destacar que apenas usuários com função ADMIN têm acesso aos endpoints de POST, DELETE e UPDATE da product-api, enquanto endpoints de GET estão disponíveis para todos os tipos de usuários.

Ao acessar o endpoint de GET cart na payment-api, é automaticamente criado um pagamento com status PENDING. Posteriormente, o status do pagamento pode ser atualizado para APPROVED ou CANCELLED, no entanto, não é possível reverter o status de PENDING.
