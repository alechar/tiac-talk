---
description: "Generate a React TypeScript functional component with props interface, data fetching hook, and CSS module"
agent: "agent"
---
Generate a React component following ALL project conventions from copilot-instructions.md.

**Component to create**: {{input}}

## Files to Generate

For a component named `XxxYyy`, create:

1. **`frontend/src/components/XxxYyy.tsx`** — Functional component with:
   - Props interface (not type alias)
   - `const` arrow function style
   - Proper TypeScript types for all props and state

2. **`frontend/src/hooks/useXxx.ts`** (if data fetching needed) — Custom hook with:
   - Loading, error, and data states
   - `fetch()` call to the backend API
   - Proper cleanup / error handling

## Requirements
- Functional components only — no class components
- TypeScript interfaces for props
- Custom hooks for data fetching logic
- Responsive layout
- Handle loading and error states in the UI
