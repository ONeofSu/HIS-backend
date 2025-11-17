# Repository Guidelines

## Project Structure & Module Organization
- Backend is split by microservice: `EurekaServer01/` (service registry), `HISGateway/` (API gateway), `HISUser/` + `UserServiceProvider/` + `UserFeign/` (auth/user), `HerbInfo/` (encyclopedia + GIS), `HISTraining/`, `HerbTeaching/`, `Research/`, `EvaluationAndDeclaration/`, `Performance/`, `HisComment/`. Each has its own `src/main/java|resources`.
- Shared assets: `Database/` (SQL schemas & seeds), `API/` (API design docs), `resources/his_info/` (uploaded samples), `HIS-Python/` (LLM service scripts).
- No parent aggregator POM; build/run services from their own directories.

## Build, Test, and Development Commands
- Build a service jar: `cd HerbInfo && mvn clean package` (applies to any service directory). Add `-DskipTests` if tests are absent.
- Run in dev: `cd HISGateway && mvn spring-boot:run` (similar for other services). Start order: `EurekaServer01` → core services → `HISGateway`.
- LLM sidecar: `cd HIS-Python && python model_server.py` (update `HerbInfo` `AIGenerateService` endpoint accordingly).
- Dependency versions: Spring Boot 3.5.x, Java 21, MySQL/PostgreSQL, Redis, RabbitMQ, Spring Cloud 2025.0.0.

## Coding Style & Naming Conventions
- Java 21, 4-space indentation, UTF-8; prefer Lombok annotations for boilerplate.
- Package names lowercase; classes/interfaces PascalCase; configs end with `Config`; controllers end with `Controller`; service interfaces `*Service`; MyBatis mappers `*Mapper`.
- REST endpoints: nouns/plurals, kebab-case paths; keep DTOs in module-local `...dto` or `...vo` packages when adding new ones.

## Testing Guidelines
- Use JUnit 5 via `spring-boot-starter-test`; place tests under `<service>/src/test/java`.
- Name test classes `<ClassName>Test`; include at least happy-path + failure-path coverage for new endpoints or mappers.
- Prefer `@SpringBootTest` only when integration is required; otherwise favor sliced tests (`@WebMvcTest`, mapper tests with embedded DB containers/mocks).

## Commit & Pull Request Guidelines
- Commit messages mirror history: short, imperative, and scoped (e.g., `modify configuration`, `add herb query filter`). Group related changes per commit.
- PRs should describe scope, runtime impact (ports/config), and DB/RabbitMQ/Redis prerequisites; link issues if any. Include screenshots for UI-facing API responses or sample payloads where helpful.

## Security & Configuration Tips
- Never commit real credentials; replace with placeholders in `application.yml`. Local defaults use MySQL/PostgreSQL on `localhost`, Redis/RabbitMQ on default ports—update before running.
- Keep Eureka host/ports consistent across services (`eureka.client.serviceUrl.defaultZone`); align gateway routes with service `spring.application.name`.
- For file uploads and AI endpoints, validate sizes and origins; keep max upload sizes aligned with front-end expectations.
