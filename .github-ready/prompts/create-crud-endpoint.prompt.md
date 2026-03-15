---
description: "Generate a complete CRUD REST endpoint with controller, service, repository, DTO record, and tests for a Java Spring Boot entity"
agent: "agent"
---
Generate a complete CRUD REST API for the following entity. Follow ALL project conventions from copilot-instructions.md.

**Entity to create**: {{input}}

## Files to Generate

For an entity named `Xxx`, create these files under `backend/src/main/java/com/demo/taskmanager/xxx/`:

1. **`Xxx.java`** — JPA `@Entity` with proper annotations
2. **`XxxDTO.java`** — Java **record** (NOT a class) with mapping methods `fromEntity()` and `toEntity()`
3. **`XxxRepository.java`** — Spring Data JPA interface
4. **`XxxService.java`** — Service with `Optional<T>` return types, never returns null
5. **`XxxController.java`** — `@RestController` with:
   - `GET /api/xxxs` — return list
   - `GET /api/xxxs/{id}` — return one or 404
   - `POST /api/xxxs` — create, return 201 with Location header
   - `PUT /api/xxxs/{id}` — update or 404
   - `DELETE /api/xxxs/{id}` — delete, return 204

And under `backend/src/test/java/com/demo/taskmanager/xxx/`:

6. **`XxxControllerTest.java`** — `@WebMvcTest` with `@DisplayName`, AssertJ, Mockito
7. **`XxxServiceTest.java`** — Unit test with `@ExtendWith(MockitoExtension.class)`

## Requirements
- Use Java 21 features: records, pattern matching, Optional
- Use `@Valid` on request bodies
- Constructor injection only
- Feature-based package structure
