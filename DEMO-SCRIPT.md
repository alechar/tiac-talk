# Demo Script — GitHub Copilot Customization (45 min)

> **Prep**: Before you start, rename `.github/` to `.github-ready/` so the project
> starts *without* customizations. You'll copy files back during the demo.
>
> ```powershell
> cd C:\projects\copilot-demo
> Rename-Item .github .github-ready
> ```
>
> To reset between dry runs:
> ```powershell
> Remove-Item .github -Recurse -Force -ErrorAction SilentlyContinue
> Rename-Item .github-ready .github
> # then rename again before the real demo
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
   > "Copilot is powerful but generic. Today I'll show how custom **instructions**,
   > **prompts**, and **agents** make it follow YOUR team's conventions automatically."

### Talking points
- "Copilot sees your code but doesn't know your team's rules"
- "Custom instructions, prompts, and agents live in `.github/` — version controlled, team-shared"
- "By the end, you'll see the same prompt produce dramatically different results"

---

## Phase 2 — Workspace & File Instructions (12 min)

### 2a. WITHOUT instructions — the "before" (3 min)

**Make sure `.github/` does NOT exist yet.**

1. Open Copilot Chat (agent mode). Type:
   > Create a Priority enum with values LOW, MEDIUM, HIGH, CRITICAL
   > and a TaskDTO with fields: id (Long), title (String), description (String),
   > priority (Priority), createdAt (LocalDateTime)

2. **Let the audience see the output.** It will likely be:
   - A plain Java class with getters/setters/constructors
   - No records, no Java 21 features
   - Generic package like `com.example` or the root package

3. **Point out** (don't fix yet):
   - *"This works, but it's pre-Java 21 style."*
   - *"No records, no sealed types, not our package convention."*
   - *"If 5 developers ask the same question, they'll get 5 different styles."*

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
3. **Show the output side by side** (or point out the differences):
   - Now uses `record TaskDTO(...)` instead of a class
   - Proper package `com.demo.taskmanager.task`
   - "Same prompt, completely different quality — because Copilot now knows our rules"

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

## Phase 3 — Reusable Prompts (10 min)

### 3a. WITHOUT prompt — the "before" (3 min)

1. Type in chat:
   > Create a complete CRUD REST API for a Category entity with fields:
   > id (Long), name (String), description (String).
   > Include controller, service, repository, DTO record, and tests.
   > Use feature-based packages under com.demo.taskmanager.category.
   > Follow Java 21 conventions with records and Optional.
   > Use @WebMvcTest for controller tests with AssertJ and @DisplayName.

2. **Point out**: 
   - *"That's a LOT of typing — and I'd have to write this every time I need a new entity."*
   - *"What if a junior dev forgets half of those instructions?"*
   - The output probably works, but you can't guarantee consistency across the team.

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

## Phase 4 — Custom Agents (12 min)

### 4a. WITHOUT agent — the "before" (3 min)

1. Make sure you're in **Agent mode** (important for the contrast).
2. Type in chat:
   > Review TaskController.java for Java 21 best practices, security issues,
   > and Spring Boot conventions

3. **Point out the risks**:
   - In agent mode, Copilot has full tool access — it might try to **edit files** even though you only wanted a review
   - The review may be generic, unstructured, and miss your team's specific conventions
   - *"I just wanted feedback, not changes. But there's no guardrail."*

### 4b. Create the code-reviewer agent — live-code (4 min)

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

## Phase 5 — MCP Brief Touch (4 min)

### What to show
1. Explain MCP in one sentence:
   > "MCP — Model Context Protocol — lets Copilot connect to external tools:
   > databases, GitHub APIs, issue trackers, anything with an MCP server."

2. Show a pre-configured example. If you have GitHub MCP set up:
   - In the agent picker or chat, ask:
     > Create a GitHub issue titled "Refactor TaskDTO to record" with the
     > findings from the code review
   - Show it creates the issue via the GitHub MCP server

3. Or show how an agent can reference MCP tools:
   ```yaml
   tools: [read, search, mcp_github/*]
   ```
   *"Your custom agents can use MCP tools — connecting Copilot to your full workflow."*

### Talking points
- *"MCP is an open standard — works with any compatible server"*
- *"Think: database queries, Jira ticket creation, Slack notifications — all from Copilot"*
- *"We're not going deep on MCP today, but it's the fourth pillar of customization"*

---

## Phase 6 — Wrap-up (2 min)

### What to show
1. Open the file tree — show the `.github/` folder with everything created during the demo:
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
   ```

2. Recap the four pillars:
   | Primitive | Purpose | Analogy |
   |-----------|---------|---------|
   | **Instructions** | How we code | Team style guide |
   | **Prompts** | What to generate | Task templates |
   | **Agents** | Who does what | Specialized team members |
   | **MCP** | What tools exist | External integrations |

3. Key takeaway:
   > "All of this is just Markdown files in `.github/`. Version controlled.
   > Team shared. No plugin to install. Every developer gets the same
   > Copilot behavior from day one."

4. Point to resources:
   - [VS Code docs: Copilot Customization](https://code.visualstudio.com/docs/copilot/customization)
   - The project they just saw is available at `C:\projects\copilot-demo`

---

## Emergency Fallback Plan

If live Copilot output is poor or slow during the demo:

1. **Pre-generate outputs**: Before the demo, run each prompt and save the outputs as
   files in a `demo-outputs/` folder. Show those if live generation fails.

2. **Switch to walkthrough mode**: Open the existing old-style code (Task.java, TaskDTO.java)
   next to what a "modern" version would look like. Explain the contrast verbally.

3. **Network issues**: The backend + frontend both run locally. MCP is the only
   part that needs network. Skip Phase 5 and give more time to Q&A.

---

## Timing Checkpoints

| Clock | You should be at... |
|-------|---------------------|
| 0:00 | Starting Phase 1 (intro) |
| 0:05 | Starting Phase 2 (instructions) |
| 0:17 | Starting Phase 3 (prompts) |
| 0:27 | Starting Phase 4 (agents) |
| 0:39 | Starting Phase 5 (MCP) |
| 0:43 | Starting Phase 6 (wrap-up) |
| 0:45 | Done |
