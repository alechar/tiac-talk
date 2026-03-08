---
description: "Use when writing Java tests, JUnit 5 test classes, Spring Boot test patterns, integration testing, unit testing, controller tests, service tests"
applyTo: "**/*Test.java"
---
# Java Testing Standards

## Test Framework
- JUnit 5 with `@DisplayName` on every test method — descriptive, sentence-style names
- AssertJ for all assertions (`assertThat(...)`) — never use JUnit's `assertEquals`
- Mockito for unit test mocking via `@ExtendWith(MockitoExtension.class)`

## Test Slicing
- Controller tests: `@WebMvcTest(XxxController.class)` — NOT full `@SpringBootTest`
- Service tests: plain unit tests with `@Mock` dependencies and `@InjectMocks`
- Repository tests: `@DataJpaTest` with `TestEntityManager`
- Full integration: `@SpringBootTest` only when testing cross-cutting concerns

## Structure
- Follow Arrange / Act / Assert pattern with blank lines separating each section
- One assertion concept per test — multiple `assertThat` calls OK if checking the same logical thing
- Test method naming: `shouldDoSomething_whenCondition`

## Example
```java
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    @DisplayName("GET /api/tasks returns 200 with list of tasks")
    void shouldReturnAllTasks_whenGetTasks() throws Exception {
        // Arrange
        var tasks = List.of(new TaskDTO(1L, "Test", "Desc", "HIGH", "TODO", LocalDateTime.now(), null));
        given(taskService.getAllTasks()).willReturn(tasks);

        // Act & Assert
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test"));
    }
}
```
