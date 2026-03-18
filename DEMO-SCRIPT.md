# Demo Script — GitHub Copilot Customization (~50 min)

> **Prep — do all of this BEFORE the demo:**
>
> ### 1. Neon DB setup
> - Create a free project at [console.neon.tech](https://console.neon.tech)
> - Copy the connection string (pooler endpoint)
> - Set environment variables (PowerShell profile or system env):
>   ```powershell
>   $env:NEON_API_KEY  = "<your-neon-api-key>"
>   $env:NEON_HOST     = "<your-project>.pooler.us-east-2.aws.neon.tech"
>   $env:NEON_DB       = "neondb"
>   $env:NEON_USER     = "<your-user>"
>   $env:NEON_PASSWORD = "<your-password>"
>   ```
> - Seed the Neon database with the schema and sample data:
>   ```sql
>   CREATE TABLE IF NOT EXISTS tasks (
>       id BIGSERIAL PRIMARY KEY,
>       title VARCHAR(255) NOT NULL,
>       description TEXT,
>       priority VARCHAR(20),
>       status VARCHAR(20) DEFAULT 'TODO',
>       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
>       updated_at TIMESTAMP
>   );
>   INSERT INTO tasks (title, description, priority, status, created_at) VALUES
>   ('Set up CI/CD pipeline', 'Configure GitHub Actions for build and deploy', 'HIGH', 'TODO', CURRENT_TIMESTAMP),
>   ('Write API documentation', 'Document all REST endpoints with OpenAPI', 'MEDIUM', 'TODO', CURRENT_TIMESTAMP),
>   ('Fix login redirect bug', 'Users are not redirected after login on mobile', 'CRITICAL', 'IN_PROGRESS', CURRENT_TIMESTAMP),
>   ('Add dark mode support', 'Implement dark mode toggle in the frontend', 'LOW', 'TODO', CURRENT_TIMESTAMP),
>   ('Database backup script', 'Create automated daily backup procedure', 'HIGH', 'TODO', CURRENT_TIMESTAMP);
>   ```
>
> ### 2. GitHub Cloud Agent prep
> - Push the repo to GitHub (with `.github/copilot-instructions.md` and instruction files)
> - Create an Issue titled **"Refactor TaskDTO from class to Java 21 record"** with body:
>   > Convert TaskDTO.java from a plain class to a Java 21 record.
>   > Update all usages in TaskService and TaskController.
>   > Ensure tests still pass.
> - Assign it to `@copilot` and let it create a PR (takes ~5 min)
> - Keep the PR open — you'll show it during Phase 6
>
> ### 3. Hide customizations
> Rename `.github/` and `.vscode/` so the project starts *without* customizations:
> ```powershell
> cd C:\projects\copilot-demo
> Rename-Item .github .github-ready
> Rename-Item .vscode .vscode-ready
> ```
>
> ### Reset between dry runs
> ```powershell
> Remove-Item .github -Recurse -Force -ErrorAction SilentlyContinue
> Remove-Item .vscode -Recurse -Force -ErrorAction SilentlyContinue
> Rename-Item .github-ready .github
> Rename-Item .vscode-ready .vscode
> # then rename both again before the real demo
> ```

---

## Phase 1 — Introduction (5 min)

### What to show
1. Open the project in VS Code. Show the file tree: `backend/` (Spring Boot), `frontend/` (React).
2. Open `TaskController.java`. Let Copilot suggest an inline completion to show code completions work.
3. Open Copilot Chat panel (Ctrl+Shift+I). Type a simple question:
   > "What does this project do?"
4. Toggle **Agent mode** (the dropdown at the top of chat). Explain:
   - *"Chat = Q&A. Agent = can read files, run commands, make edits."*
5. Briefly mention the goal:
   > "Copilot is powerful but generic. Today I'll show five ways to customize it:
   > **instructions**, **prompts**, **agents**, **MCP** for connecting to live databases,
   > and the **cloud coding agent** that creates PRs while you sleep."

### Talking points
- "Copilot sees your code but doesn't know your team's rules"
- "Custom instructions, prompts, agents, and MCP config live in `.github/` and `.vscode/` — version controlled, team-shared"
- "By the end, you'll see the same prompt produce dramatically different results — and Copilot querying a live Postgres database"

---

## Phase 2 — Workspace & File Instructions (10 min)

### 2a. WITHOUT instructions — the "before" (2 min)

**Make sure `.github/` does NOT exist yet.**

1. Open Copilot Chat (agent mode). Type:
   Add a Tag feature to this task management app. Create a TagDTO with fields id (Long) and name (String), a TagRepository extending JpaRepository, and a TagService with findAll(), findById(Long id), and create(TagDTO dto) methods. Also add a TagController with GET /api/tags and POST /api/tags endpoints.

2. **Let the audience see the output.** Watch for these specific details:

3. **Point out** (don't fix yet — these are your guaranteed talking points):
   - **Package**: It will use something like `com.example.demo`, `com.taskmanager`, or just `com.demo.taskmanager` — *not* the feature-scoped `com.demo.taskmanager.tag`. This is always wrong without instructions.
   - **`findById` return type**: Likely returns `Tag` or `null` directly, not `Optional<Tag>`.
   - **POST endpoint**: Likely returns `200 OK` with the saved entity — our conventions require `201 Created` with a `Location` header.
   - **Injection**: May use `@Autowired` field injection instead of constructor injection.

   > **If the output happens to look correct on some of these:** That's actually your point — *"It guessed right this time. But what about the next developer? What about tomorrow? With no instructions, every answer is a coin flip. With instructions, it's always consistent."*

### 2b. Add workspace instructions — live-code (4 min)

1. Create the folder and file live:
   - File → New Folder: `.github`
   - File → New File: `.github/copilot-instructions.md`
   
   **Or** copy from backup:
   ```powershell
   mkdir .github
   copy .github-ready\copilot-instructions.md .github\
   ```

2. Walk through the file on screen. Highlight the key rules:
   - "**Records** for DTOs — not classes"
   - "**Optional** return types, never null"
   - "**Feature-based packages** like `com.demo.taskmanager.task`"
   - "React: functional components, TypeScript interfaces"

3. Explain: *"This file is automatically included in every Copilot interaction in this workspace."*

### 2c. Re-ask the SAME question (2 min)

1. **Open a fresh chat** (important — so the audience sees the clean comparison).
2. Paste the exact same prompt from step 2a.
3. **Show the output and call out each difference:**
   - **Package**: Now `com.demo.taskmanager.tag` — feature-based, exactly as the instruction says.
   - **`TagDTO`**: Now a `record TagDTO(Long id, String name)` — no getters, no boilerplate.
   - **`findById`**: Now returns `Optional<Tag>` — never null.
   - **POST endpoint**: Now returns `ResponseEntity.created(uri).body(tag)` with `201 Created` and a `Location` header.
   - **Service wiring**: Constructor injection with `final` field — no `@Autowired`.
   - *"Same prompt. Five concrete differences. Because Copilot now knows our rules."*

### 2d. Add file-specific instruction (3 min)

1. Create `.github/instructions/` folder and `java-testing.instructions.md`.
   
   **Or** copy from backup:
   ```powershell
   mkdir .github\instructions
   copy .github-ready\instructions\java-testing.instructions.md .github\instructions\
   ```

2. Open the file. Highlight:
   - `applyTo: "**/*Test.java"` — "This instruction ONLY activates when working with test files"
   - JUnit 5, `@WebMvcTest`, AssertJ, `@DisplayName` conventions

3. Quick test: ask Copilot:
   > Write a test for TaskController's GET /api/tasks endpoint

4. Show the output uses `@WebMvcTest`, `@DisplayName`, AssertJ — all matching the instruction.

### Talking points
- *"Workspace instructions = always on. File instructions = activated by file pattern."*
- *"Both are checked into Git. Everyone on the team gets the same Copilot behavior."*
- *"Note: we didn't change any code — just added a markdown file."*

---

## Phase 3 — Reusable Prompts (8 min)

### 3a. WITHOUT prompt — the "before" (2 min)

1. Flash this in chat (don't wait for full output — the point is the wall of text):
   > Create a complete CRUD REST API for a Category entity with fields:
   > id (Long), name (String), description (String).
   > Include controller, service, repository, DTO record, and tests.
   > Use feature-based packages under com.demo.taskmanager.category.
   > Follow Java 21 conventions with records and Optional.
   > Use @WebMvcTest for controller tests with AssertJ and @DisplayName.

2. **Point out**: 
   - *"That's a LOT of typing — every time, for every entity."*
   - *"What if a junior dev forgets half of those instructions?"*

### 3b. Create a reusable prompt — live-code (4 min)

1. Create `.github/prompts/create-crud-endpoint.prompt.md`.
   
   **Or** copy:
   ```powershell
   mkdir .github\prompts
   copy .github-ready\prompts\create-crud-endpoint.prompt.md .github\prompts\
   ```

2. Walk through the file on screen:
   - **Frontmatter**: `description` (for discoverability), `agent: "agent"` (so it can create files)
   - **Body**: structured template — lists exactly which files to generate, which patterns to follow
   - *"`{{input}}` is replaced by whatever you type after the slash command"*

3. Explain: *"This is a reusable recipe. Type it once, the whole team uses it forever."*

### 3c. Use the prompt (3 min)

1. In chat, type `/` — show the prompt appearing in the list.
2. Select `/create-crud-endpoint`.
3. Type:
   > Category entity with id (Long), name (String), description (String)

4. **Watch it generate** all 7 files. Point out:
   - DTO is a record (from workspace instructions)
   - Tests use `@WebMvcTest` + `@DisplayName` (from file instructions)
   - Package structure is `com.demo.taskmanager.category` (from prompt template)
   - *"One slash command. Consistent every time. For every developer."*

### Bonus: React prompt (if time allows, 1 min)

1. Copy the React prompt:
   ```powershell
   copy .github-ready\prompts\create-react-component.prompt.md .github\prompts\
   ```
2. Type `/create-react-component` → "CategoryList component that fetches from /api/categories"
3. Show the generated component follows React/TS conventions from workspace instructions.

### Talking points
- *"Prompts compose with instructions — the prompt says WHAT, the instructions say HOW"*
- *"They appear in the `/` menu, easy to discover, no documentation needed"*
- *"This is how you scale team practices without writing a wiki nobody reads"*

---

## Phase 4 — Custom Agents (10 min)

### 4a. WITHOUT agent — the "before" (2 min)

1. Make sure you're in **Agent mode** (important for the contrast).
2. Type in chat:
   > Review TaskController.java for Java 21 best practices, security issues,
   > and Spring Boot conventions

3. **Point out the risks**:
   - In agent mode, Copilot has full tool access — it might try to **edit files** even though you only wanted a review
   - *"I just wanted feedback, not changes. But there's no guardrail."*

### 4b. Create the code-reviewer agent — live-code (3 min)

1. Create `.github/agents/code-reviewer.agent.md`.
   
   **Or** copy:
   ```powershell
   mkdir .github\agents
   copy .github-ready\agents\code-reviewer.agent.md .github\agents\
   ```

2. Walk through the file on screen:
   - **`tools: [read, search]`** — *"Read-only. This agent CANNOT modify files. Safe."*
   - **Persona**: senior Java 21 + React reviewer
   - **Review checklist**: records, Optional, ResponseEntity, no @Autowired fields, security
   - **Output format**: categorized by severity (Critical / Warning / Info / What's Good)
   - **`description`** — *"This is how Copilot discovers the agent. Keywords matter."*

### 4c. Use @code-reviewer (2 min)

1. In the agent picker dropdown (top of chat), select `@code-reviewer`.
2. Type:
   > Review TaskController.java and TaskService.java

3. **Show the structured output**:
   - 🔴 Critical: might flag null returns in service
   - 🟡 Warning: TaskDTO is a class not a record, no `@Valid`, missing logging
   - 🔵 Info: could use `ResponseEntity.created()` with Location header
   - ✅ Good: feature-based packages, constructor injection
   - *"Structured, actionable, and it can't accidentally break anything."*

### 4d. Create the test-writer agent (3 min)

1. Copy the agent file:
   ```powershell
   copy .github-ready\agents\test-writer.agent.md .github\agents\
   ```

2. Briefly show the file:
   - **`tools: [read, search, edit]`** — *"This one CAN create files — because its job is to write tests."*
   - Reads source code, reads testing instructions, generates tests matching conventions

3. Select `@test-writer` from the agent picker. Type:
   > Write comprehensive tests for TaskService

4. **Show it generates** a proper test file with:
   - `@ExtendWith(MockitoExtension.class)`
   - `@DisplayName` on every test
   - AssertJ assertions
   - Tests for happy path, not-found cases, and edge cases
   - *"The reviewer can't edit. The test writer can. Each agent has exactly the tools it needs."*

### Talking points
- *"Agents are like specialized team members — a reviewer, a test writer, a security auditor"*
- *"Tool restrictions = guardrails. You decide what each agent can do."*
- *"They compose with instructions AND prompts — the whole system works together"*

---

## Phase 5 — MCP & Neon DB (10 min)

### 5a. What is MCP? (1 min)

1. Explain MCP in one sentence:
   > "MCP — Model Context Protocol — lets Copilot connect to external tools:
   > databases, GitHub APIs, issue trackers, anything with an MCP server."

2. *"Today we'll connect Copilot to a live Neon PostgreSQL database — serverless Postgres in the cloud. Copilot will query real data, not just read files."*

### 5b. Set up Neon MCP — live-code (2 min)

1. Create the `.vscode/` folder and `mcp.json` live:
   ```powershell
   mkdir .vscode
   copy .vscode-ready\mcp.json .vscode\
   ```

2. Open `.vscode/mcp.json` on screen. Walk through:
   - **`"neon"`** — server name, shows up in Copilot's tool list
   - **`"url": "https://mcp.neon.tech/sse"`** — connects to Neon's hosted MCP server, no local install needed
   - **`Authorization` header** — authenticates with your Neon API key
   - *"This is all it takes — one JSON file, and Copilot can talk to your database."*

3. Show VS Code detecting the MCP server: the "Start" button appears in chat's tool picker. **Click Start** to connect.

### 5c. Query the live database from chat (3 min)

1. In Copilot Chat (agent mode), type:
   > List all tables in my Neon database

   - Show it runs a query and returns the `tasks` table.

2. Then ask:
   > Show me all tasks with priority HIGH

   - It runs `SELECT * FROM tasks WHERE priority = 'HIGH'` and returns the results.

3. Then ask:
   > What's the distribution of task statuses? Show as a summary.

   - It runs an aggregate query and presents the counts.

4. **Point out**:
   - *"Copilot just ran real SQL against a live Postgres instance — not reading files, not guessing."*
   - *"This is your staging database, your analytics DB, your production read-replica — whatever you connect."*

### 5d. Switch the backend to Neon with Agent mode (3 min)

1. Switch to **Agent mode**. Type:
   > Switch our Spring Boot backend from H2 to the Neon PostgreSQL database.
   > Add the PostgreSQL driver to pom.xml and create an application-neon.properties
   > profile with connection settings from environment variables.

2. **Watch Copilot**:
   - Add `postgresql` dependency to `pom.xml`
   - Create `application-neon.properties` with the Neon connection string
   - *"Agent mode reads, edits, and runs commands. Combined with MCP's database knowledge, it just migrated our data layer."*

3. (If time) Run the backend with the Neon profile to prove it connects:
   ```powershell
   cd backend
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=neon
   ```

### 5e. Talking points (1 min)
- *"MCP is an open standard — Neon, GitHub, Jira, Slack, any compatible server"*
- *"Your custom agents can use MCP tools too"*:
  ```yaml
  tools: [read, search, mcp_neon/*]
  ```
- *"This turns Copilot from a code helper into a full-stack assistant that understands your live data"*

---

## Phase 6 — Cloud Coding Agent (5 min)

### 6a. What is the Copilot coding agent? (1 min)

1. *"Everything we've seen runs locally in VS Code. But Copilot can also work autonomously in the cloud."*
2. *"You assign a GitHub Issue to Copilot, and it opens a PR — reading your instructions, writing code, running tests, iterating on failures."*
3. *"It uses the SAME `.github/copilot-instructions.md` and file instructions you already set up — your customizations carry over."*

### 6b. Show the Issue assignment (1 min)

1. Open **GitHub in the browser**. Navigate to the repo.
2. Show the pre-created Issue: **"Refactor TaskDTO from class to Java 21 record"**
3. Point out: the issue is assigned to **`@copilot`**.
4. *"I assigned this before the talk. Here's what happened while we were presenting..."*

### 6c. Walk through the PR (2 min)

1. Open the **PR that Copilot created** from the Issue.
2. Walk through the diff:
   - `TaskDTO.java` converted from a class with getters/setters to a `record`
   - Usages in `TaskService.java` and `TaskController.java` updated
   - Tests updated or still passing
3. **Point out key observations**:
   - *"It followed our `copilot-instructions.md` — records for DTOs, Optional return types"*
   - *"It read the testing instructions and used `@DisplayName`, AssertJ"*
   - *"It ran tests, found failures, and iterated until they passed"*

### 6d. Talking points (1 min)
- *"Cloud agent is async — assign the issue, go for coffee, review the PR when you're back"*
- *"Best for well-scoped, clearly-described issues with good instructions"*
- *"Think: refactors, dependency upgrades, boilerplate tasks, migration scripts"*
- *"Your `.github/` customizations are the secret sauce — the better your instructions, the better the automated PRs"*

---

## Phase 7 — Wrap-up (2 min)

### What to show
1. Open the file tree — show everything created during the demo:
   ```
   .github/
   ├── copilot-instructions.md          ← "How we code"
   ├── instructions/
   │   └── java-testing.instructions.md ← "How we test"
   ├── prompts/
   │   ├── create-crud-endpoint.prompt.md    ← "What to generate"
   │   └── create-react-component.prompt.md  ← "What to generate"
   └── agents/
       ├── code-reviewer.agent.md       ← "Who reviews"
       └── test-writer.agent.md         ← "Who tests"
   .vscode/
   └── mcp.json                         ← "What tools Copilot can reach"
   ```

2. Recap the five pillars:
   | Primitive | Purpose | Analogy |
   |-----------|---------|---------|
   | **Instructions** | How we code | Team style guide |
   | **Prompts** | What to generate | Task templates |
   | **Agents** | Who does what | Specialized team members |
   | **MCP** | What tools exist | External integrations |
   | **Cloud Agent** | Async autonomy | Junior dev on the team |

3. Key takeaway:
   > "All of this is Markdown files in `.github/` and a JSON file in `.vscode/`.
   > Version controlled. Team shared. No plugin to install.
   > Every developer — and even the cloud agent — gets the same
   > Copilot behavior from day one."

4. Point to resources:
   - [VS Code docs: Copilot Customization](https://code.visualstudio.com/docs/copilot/customization)
   - [Neon MCP Server](https://neon.tech/docs/ai/neon-mcp-server)
   - [GitHub Copilot Coding Agent](https://docs.github.com/en/copilot/using-github-copilot/using-the-copilot-coding-agent)

---

## Emergency Fallback Plan

If live Copilot output is poor or slow during the demo:

1. **Pre-generate outputs**: Before the demo, run each prompt and save the outputs as
   files in a `demo-outputs/` folder. Show those if live generation fails.

2. **Switch to walkthrough mode**: Open the existing old-style code (Task.java, TaskDTO.java)
   next to what a "modern" version would look like. Explain the contrast verbally.

3. **Network issues**: The backend + frontend both run locally on H2. MCP (Phase 5) and
   the cloud agent PR (Phase 6) need network. If the network is down:
   - **Phase 5**: Show the `.vscode/mcp.json` config and pre-recorded screenshots of the
     Neon queries. Explain verbally — the config file is still impactful to see.
   - **Phase 6**: Show the pre-created PR in screenshots instead of live GitHub.
   - Give the recovered time to Q&A.

---

## Timing Checkpoints

| Clock | You should be at... |
|-------|---------------------|
| 0:00 | Starting Phase 1 (intro) |
| 0:05 | Starting Phase 2 (instructions) |
| 0:15 | Starting Phase 3 (prompts) |
| 0:23 | Starting Phase 4 (agents) |
| 0:33 | Starting Phase 5 (MCP & Neon DB) |
| 0:43 | Starting Phase 6 (cloud coding agent) |
| 0:48 | Starting Phase 7 (wrap-up) |
| 0:50 | Done |
