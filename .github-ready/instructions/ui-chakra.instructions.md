---
applyTo: "frontend/**"
---

# UI Guidelines — Chakra UI

## Core Rule

Use **Chakra UI exclusively** for all UI — components, layout, spacing, color, and typography.  
Never use:
- Raw HTML elements styled with CSS classes (e.g. no `<div className="card">`)
- Global CSS files or CSS Modules for component styling
- Inline `style={{}}` props
- Any other component library (MUI, Ant Design, Tailwind, etc.)

---

## Setup

Wrap the app root with `ChakraProvider` once, in `main.tsx`:

```tsx
import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { ChakraProvider, defaultSystem } from '@chakra-ui/react';
import App from './App';

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <ChakraProvider value={defaultSystem}>
      <App />
    </ChakraProvider>
  </StrictMode>
);
```

---

## TypeScript — Never Use `any`

All props, state, API responses, and event handlers must be fully typed.  
Use specific types from React or define your own interfaces.

```tsx
// WRONG
const handleChange = (e: any) => { ... };

// CORRECT
const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => { ... };
```

```tsx
// WRONG
const [data, setData] = useState<any>(null);

// CORRECT
interface User {
  id: number;
  username: string;
}
const [user, setUser] = useState<User | null>(null);
```

---

## Layout

Use Chakra layout primitives — `Box`, `Flex`, `Stack`, `VStack`, `HStack`, `Grid`, `Container`.

```tsx
import { Container, VStack, HStack, Box } from '@chakra-ui/react';

const PageLayout = () => (
  <Container maxW="4xl" py={8}>
    <VStack gap={6} align="stretch">
      <HStack justify="space-between">
        <Box fontWeight="bold">Title</Box>
        <Box color="gray.500">Subtitle</Box>
      </HStack>
    </VStack>
  </Container>
);
```

---

## Typography

Use the `Text` and `Heading` components instead of raw `<p>` or `<h1>`–`<h6>` tags.

```tsx
import { Heading, Text } from '@chakra-ui/react';

const PageHeader = () => (
  <>
    <Heading size="xl">Task Manager</Heading>
    <Text color="gray.500" mt={1}>Manage your tasks efficiently</Text>
  </>
);
```

---

## Forms

Use Chakra's `Field`, `Input`, `Button`, and `NativeSelect` for all form elements.  
Always associate labels with inputs via the `Field` wrapper — never use raw `<label>` tags.

```tsx
import { Button, Field, Input, Stack } from '@chakra-ui/react';
import { useState } from 'react';

interface LoginCredentials {
  username: string;
  password: string;
}

interface LoginFormProps {
  onLogin: (credentials: LoginCredentials) => Promise<void>;
}

const LoginForm = ({ onLogin }: LoginFormProps) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError(null);
    setLoading(true);
    try {
      await onLogin({ username, password });
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Login failed.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <Stack gap={4} maxW="sm" mx="auto" mt={16}>
        <Heading size="lg">Sign in</Heading>
        {error && <Text color="red.500">{error}</Text>}

        <Field.Root required>
          <Field.Label>Username</Field.Label>
          <Input
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            placeholder="Enter your username"
            autoComplete="username"
            disabled={loading}
          />
        </Field.Root>

        <Field.Root required>
          <Field.Label>Password</Field.Label>
          <Input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Enter your password"
            autoComplete="current-password"
            disabled={loading}
          />
        </Field.Root>

        <Button
          type="submit"
          colorScheme="purple"
          loading={loading}
          disabled={!username || !password}
        >
          Sign in
        </Button>
      </Stack>
    </form>
  );
};
```

---

## Feedback — Loading & Errors

Use `Spinner` for loading states and `Alert` for error and success messages.

```tsx
import { Spinner, Alert, Center } from '@chakra-ui/react';

// Loading
<Center py={16}>
  <Spinner size="xl" color="purple.500" />
</Center>

// Error
<Alert.Root status="error" borderRadius="md">
  <Alert.Indicator />
  <Alert.Title>Failed to load tasks.</Alert.Title>
</Alert.Root>

// Success
<Alert.Root status="success" borderRadius="md">
  <Alert.Indicator />
  <Alert.Title>Task created successfully.</Alert.Title>
</Alert.Root>
```

---

## Badges & Status Indicators

Use the `Badge` component for status and priority labels.

```tsx
import { Badge, HStack } from '@chakra-ui/react';

const priorityColor: Record<string, string> = {
  CRITICAL: 'red',
  HIGH: 'orange',
  MEDIUM: 'yellow',
  LOW: 'green',
};

const statusColor: Record<string, string> = {
  TODO: 'blue',
  IN_PROGRESS: 'orange',
  DONE: 'green',
};

interface TaskBadgesProps {
  priority: string;
  status: string;
}

const TaskBadges = ({ priority, status }: TaskBadgesProps) => (
  <HStack>
    <Badge colorScheme={priorityColor[priority]}>{priority}</Badge>
    <Badge colorScheme={statusColor[status]}>{status.replace('_', ' ')}</Badge>
  </HStack>
);
```

---

## Cards & Lists

Use `Card` for grouped content; use `For` or `.map()` with `Stack` for lists.

```tsx
import { Card, Stack, Text, Heading } from '@chakra-ui/react';
import type { Task } from '../types';

interface TaskCardProps {
  task: Task;
}

const TaskCard = ({ task }: TaskCardProps) => (
  <Card.Root>
    <Card.Body>
      <Heading size="sm">{task.title}</Heading>
      {task.description && <Text color="gray.600" mt={1}>{task.description}</Text>}
    </Card.Body>
  </Card.Root>
);

interface TaskListProps {
  tasks: Task[];
}

const TaskList = ({ tasks }: TaskListProps) => (
  <Stack gap={4}>
    {tasks.map((task) => (
      <TaskCard key={task.id} task={task} />
    ))}
  </Stack>
);
```

---

## Modals & Dialogs

Use Chakra's `Dialog` for confirmations and detail views; never use browser `confirm()`.

```tsx
import { Button, Dialog, Portal, Text } from '@chakra-ui/react';

interface DeleteConfirmProps {
  onConfirm: () => void;
}

const DeleteConfirm = ({ onConfirm }: DeleteConfirmProps) => (
  <Dialog.Root>
    <Dialog.Trigger asChild>
      <Button colorScheme="red" variant="outline">Delete</Button>
    </Dialog.Trigger>
    <Portal>
      <Dialog.Backdrop />
      <Dialog.Positioner>
        <Dialog.Content>
          <Dialog.Header>
            <Dialog.Title>Delete task?</Dialog.Title>
          </Dialog.Header>
          <Dialog.Body>
            <Text>This action cannot be undone.</Text>
          </Dialog.Body>
          <Dialog.Footer>
            <Dialog.ActionTrigger asChild>
              <Button variant="ghost">Cancel</Button>
            </Dialog.ActionTrigger>
            <Button colorScheme="red" onClick={onConfirm}>Delete</Button>
          </Dialog.Footer>
        </Dialog.Content>
      </Dialog.Positioner>
    </Portal>
  </Dialog.Root>
);
```

---

## Component Checklist

| Concern | Use |
|---|---|
| Layout / spacing | `Box`, `Flex`, `Stack`, `VStack`, `HStack`, `Grid`, `Container` |
| Typography | `Heading`, `Text` |
| Form fields | `Field.Root`, `Field.Label`, `Input`, `Textarea`, `NativeSelect` |
| Buttons | `Button`, `IconButton` |
| Feedback | `Spinner`, `Alert`, `Toast` |
| Status labels | `Badge` |
| Grouped content | `Card.Root`, `Card.Body` |
| Dialogs | `Dialog.Root` and related sub-components |
| Navigation | `Tabs`, `Link` |
| Icons | `@chakra-ui/icons` or `react-icons` |
