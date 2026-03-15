# Project Guidelines

## Tech Stack
- **Backend**: Java 21 with Spring Boot 3.x
- **Frontend**: React 18 with TypeScript, Vite, functional components only
- **Database**: Spring Data JPA (H2 for dev, PostgreSQL for prod)

## Java 21 Conventions
- Use **records** for DTOs and value objects — never plain classes with getters/setters
- Use **sealed interfaces** to model closed type hierarchies (e.g., domain events, command results)
- Use **pattern matching with switch** instead of if/else chains or visitor pattern
- Use **text blocks** (`"""`) for multi-line strings (SQL, JSON templates)
- Prefer `Optional<T>` for nullable return types — never return null from service methods
- Use `.toList()` instead of `.collect(Collectors.toList())`

## Spring Boot Conventions
- REST controllers: `@RestController` + `ResponseEntity<T>` for all endpoints
- Package structure: **feature-based** (e.g., `com.demo.taskmanager.task`, `com.demo.taskmanager.user`)
- Constructor injection only — no `@Autowired` on fields
- Validate request bodies with `@Valid` + Jakarta Validation annotations
- Return proper HTTP status codes: 201 for creation with Location header, 204 for deletion

## React / TypeScript Conventions
- Functional components only — no class components
- Use TypeScript interfaces for props (not `type` aliases for simple objects)
- Custom hooks for data fetching and shared logic
- Prefer `const` arrow functions for components: `const MyComponent = () => { ... }`
- CSS Modules or inline styles — no global CSS classes for components
- NEVER use `any` type — always define proper types for props, state, and API responses

## Code Quality
- No `System.out.println` — use SLF4J logger
- All public API methods must have concise Javadoc
- Prefer immutability: records, unmodifiable collections, final variables
