# Agendamento Médico — Backend API RESTful

API RESTful de nível profissional para o gerenciamento de consultas em clínicas médicas, abrangendo módulos de autenticação, médicos, pacientes e agendamentos. Desenvolvida para demonstrar a aplicação prática de arquitetura em camadas, princípios **SOLID**, validações de regras de negócio desacopladas, segurança **Stateless com JWT** e **testes automatizados**.

O projeto foi construído pensando na integração com um futuro ecossistema **Full-Stack**, contando com suporte nativo a **CORS**, tratamento global de exceções e documentação via **Swagger/OpenAPI 3**.

---

## Autor

**Matheus Húngaro Martins da Silva**  
 Estudante de **Análise e Desenvolvimento de Sistemas**, com previsão de formatura em 07/2027
 GitHub: [github.com/matheus-hungaro](https://github.com/matheus-hungaro)

> *Este projeto foi desenvolvido com o propósito de consolidar um portfólio robusto e demonstrar capacidade técnica na construção de serviços Backend modernos, seguros e escaláveis em Java com Spring Boot.*

---

## Tecnologias e Ferramentas

| Categoria | Tecnologia / Biblioteca | Descrição / Uso |
| :--- | :--- | :--- |
| **Linguagem** | Java 17 | Uso de recursos modernos como Records, Sealed Types e Pattern Matching. |
| **Framework** | Spring Boot 3.3.6 | Base para injeção de dependências, Spring MVC e configuração do ecossistema. |
| **Segurança** | Spring Security + Auth0 JWT | Autenticação Stateless por tokens JWT com controle de expiração (2h). |
| **Persistência** | Spring Data JPA / Hibernate | Mapeamento relacional e abstração da camada de dados com consultas customizadas. |
| **Banco de Dados** | MySQL 8.0 / H2 Database | MySQL para ambiente de desenvolvimento local e H2 em memória para testes. |
| **Migrações** | Flyway Migration | Versionamento e controle evolutivo do schema do banco de dados relacional. |
| **Validação** | Jakarta Validation (Bean Validation) | Validações declarativas em DTOs (`@NotBlank`, `@Email`, `@Pattern`, `@Future`). |
| **Documentação**| SpringDoc OpenAPI / Swagger UI | Interface interativa para navegação e consumo dos endpoints da API. |
| **Testes** | JUnit 5, Mockito, Spring Security Test | Suíte para testes unitários isolados e testes de integração de controllers (`MockMvc`).|
| **Produtividade**| Lombok & Maven | Redução de boilerplate (Getters, Setters, Construtores) e gestão de dependências. |

---

## Arquitetura e Decisões de Engenharia de Software

### 1. Padrão DTO com Java Records
A comunicação entre o cliente e os controllers (e entre os controllers e os services) é feita estritamente através de **Data Transfer Objects (DTOs)** implementados via Java `records`. Isso impede a exposição indevida das entidades JPA, assegura imutabilidade dos dados trafegados e evita ataques do tipo *Mass Assignment*.

### 2. Validações Desacopladas com Polimorfismo (Princípio SOLID)
A regra de agendamento de consultas não contém blocos extensos de `if` encadeados no serviço. Cada regra de negócio é uma classe independente anotada com `@Component` que implementa a interface `ValidadorAgendamentoDeConsulta`. 

A classe `AgendaDeConsultasService` recebe a lista dinâmica de validadores pelo construtor (`List<ValidadorAgendamentoDeConsulta> validadores`) e executa o método `.validar(dados)` em um `forEach`. Isso aplica diretamente:
- **Single Responsibility Principle (SRP):** Cada validador cuida de apenas uma regra específica.
- **Open/Closed Principle (OCP):** Para criar uma nova regra de agendamento, basta criar uma nova classe que implementa a interface, sem alterar o código do serviço principal.

### 3. Soft Delete (Exclusão Lógica)
Para evitar perda de histórico de consultas atreladas a médicos ou pacientes desativados, a exclusão nos endpoints `DELETE /medicos/{id}` e `DELETE /pacientes/{id}` não remove o registro da tabela. Em vez disso, altera o atributo `ativo` para `false` (`medico.desativar()`), garantindo a integridade referencial dos relatórios e do histórico do sistema.

### 4. Tratamento Global e Padronizado de Erros
A classe `@RestControllerAdvice` (`TratadorDeErros`) intercepta falhas da aplicação e converte em respostas HTTP coerentes:
- **HTTP 400 (Bad Request):** Para campos de DTO inválidos (retorna um JSON com o nome do campo e mensagem) ou violações de regras de negócio.
- **HTTP 401 (Unauthorized):** Para falhas na autenticação JWT ou credenciais incorretas.
- **HTTP 403 (Forbidden):** Para tentativas de acesso a rotas sem a permissão necessária.
- **HTTP 404 (Not Found):** Quando uma entidade não é encontrada via `EntityNotFoundException`.
- **HTTP 500 (Internal Server Error):** Para exceções não mapeadas do servidor.

---

## Módulos e Regras de Negócio do Sistema

### 1. Autenticação (`/login`)
- Endpoint público de autenticação que recebe `login` e `senha`.
- Senhas são armazenadas no banco de dados utilizando a criptografia de *hash* **BCrypt**.
- Ao autenticar com sucesso, emite um token JWT assinado com a chave secreta configurada e validade de 2 horas.

### 2. Gestão de Médicos (`/medicos`)
- **Cadastro (`POST /medicos`):** Requer nome, e-mail, telefone, CRM (4 a 6 dígitos), especialidade (`CARDIOLOGIA`, `DERMATOLOGIA`, `ORTOPEDIA`, `GINECOLOGIA`) e endereço completo. Retorna `201 Created` acompanhado do cabeçalho `Location`.
- **Listagem (`GET /medicos`):** Retorna uma página paginada (`Pageable`) apenas com os médicos ativamente cadastrados (`ativo = true`).
- **Atualização (`PUT /medicos`):** Permite alterar o nome, e-mail e endereço de um médico ativo.
- **Desativação (`DELETE /medicos/{id}`):** Realiza o *Soft Delete* no banco de dados.

### 3. Gestão de Pacientes (`/pacientes`)
- **Cadastro (`POST /pacientes`):** Requer nome, e-mail, CPF (11 dígitos), telefone e endereço.
- **Listagem (`GET /pacientes`):** Retorna uma lista paginada dos pacientes ativos.
- **Desativação (`DELETE /pacientes/{id}`):** Desativa o cadastro do paciente.

### 4. Agendamento e Cancelamento de Consultas (`/consultas`)
- **Agendamento (`POST /consultas`):** Valida automaticamente:
  - **Horário de Funcionamento:** Segunda a Sábado, das 07:00 às 19:00.
  - **Antecedência Mínima:** A consulta deve ser agendada com no mínimo 30 minutos de antecedência.
  - **Status dos Envolvidos:** Tanto o médico quanto o paciente devem estar ativos no sistema.
- **Cancelamento (`DELETE /consultas`):** Requer o ID da consulta e o motivo obrigatorio do cancelamento (`PACIENTE_DESISTIU`, `MEDICO_CANCELOU`, `OUTROS`).
  - **Antecedência do Cancelamento:** A consulta só pode ser cancelada com no mínimo 24 horas de antecedência do horário agendado.

---

## 🛠️ Desafios Reais Enfrentados e Soluções Adotadas

Durante o desenvolvimento do projeto, enfrentei e solucionei diversos desafios práticos que elevaram a qualidade do código:

1. **Gargalo e Incompatibilidade nos DTOs Embutidos no Hibernate:**
   - *Desafio:* A entidade `Medico` exige dados de `Endereco` marcados como `NOT NULL` no schema do MySQL. A simplificação de DTOs causava erros de `DataIntegrityViolationException` durante as chamadas do controller.
   - *Solução:* Reestruturação e padronização dos DTOs de cadastro e atualização para compor objetos `DadosEndereco` anotados com `@Valid`, garantindo que os dados cheguem completos até a camada de persistência.

2. **Isolamento de Ambiente de Testes com H2:**
   - *Desafio:* Garantir que a execução da suíte de testes unitários e de integração pelo Maven (`./mvnw clean test`) rodesse de forma limpa, isolada e rápida sem depender de um banco MySQL ativo na máquina ou poluir a base de dados local.
   - *Solução:* Criação do perfil `application-test.properties` configurando um banco em memória H2 com `ddl-auto=create-drop` e desativação temporária do Flyway no contexto de testes.

3. **Incompatibilidade de CORS com o Futuro Frontend:**
   - *Desafio:* Permitir que requisições vindas de clientes HTTP no navegador (ex: aplicações em React/Vite rodando na porta `5173`) pudessem autenticar e consumir a API REST sem sofrer bloqueio de origem pelo navegador.
   - *Solução:* Configuração explícita da chave `.cors()` no `SecurityFilterChain` com um `CorsConfigurationSource` especificando origins, headers e métodos HTTP permitidos.

---

## 🚀 Como Executar o Projeto Localmente

### Pré-requisitos
- **Java 17** instalado e configurado nas variáveis de ambiente.
- **MySQL 8.0** em execução na porta `3306`.

### Instalação e Execução

1. **Clonar o Repositório:**
   ```bash
   git clone [https://github.com/matheus-hungaro/agendamento-medico-api.git](https://github.com/matheus-hungaro/agendamento-medico-api.git)
   cd agendamento-medico-api