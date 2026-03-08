# GitHub Copilot Customization Demo

A 45-minute demo showing how **custom instructions, prompts, and agents** dramatically improve GitHub Copilot's output vs. ad-hoc chat prompts.

## Project: Task Manager

| Layer | Tech |
|-------|------|
| Backend | Spring Boot 3.x, Java 21, H2 in-memory DB |
| Frontend | React 18, TypeScript, Vite |

## Demo Flow

| Phase | Duration | What |
|-------|----------|------|
| 1. Intro | 5 min | What is Copilot? Show chat/agent mode |
| 2. Instructions | 12 min | Before/after: `copilot-instructions.md` + file instructions |
| 3. Prompts | 10 min | Before/after: reusable `/create-crud-endpoint` prompt |
| 4. Agents | 12 min | Before/after: `@code-reviewer` + `@test-writer` agents |
| 5. MCP | 4 min | Brief: external tool integration |
| 6. Wrap-up | 2 min | Recap `.github/` folder, key takeaways |

## Quick Start

```bash
# Backend
cd backend
./mvnw spring-boot:run

# Frontend (separate terminal)
cd frontend
npm install
npm run dev
```

## `.github/` Customization Files

| File | Type | Purpose |
|------|------|---------|
| `copilot-instructions.md` | Workspace instructions | Java 21 + React conventions |
| `instructions/java-testing.instructions.md` | File instructions | Test patterns (auto-applies to `*Test.java`) |
| `prompts/create-crud-endpoint.prompt.md` | Prompt | CRUD generation template |
| `prompts/create-react-component.prompt.md` | Prompt | React component template |
| `agents/code-reviewer.agent.md` | Agent | Read-only code reviewer |
| `agents/test-writer.agent.md` | Agent | Test generation specialist |

## Demo Preparation

1. Open this folder in VS Code
2. Ensure GitHub Copilot extension is installed and active
3. **Before demo**: delete or move the `.github/` folder aside (rename to `.github-ready/`)
4. **During demo**: recreate files live (or copy them back from `.github-ready/`)
5. Use the `DEMO-SCRIPT.md` for step-by-step speaker notes
