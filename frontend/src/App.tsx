import { useState, useEffect } from 'react';
import type { Task } from './types';
import TaskList from './components/TaskList';
import './App.css';

function App() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetch('/api/tasks')
      .then((res) => {
        if (!res.ok) throw new Error('Failed to fetch tasks');
        return res.json();
      })
      .then((data) => setTasks(data))
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="app">
      <h1>Task Manager</h1>
      <p className="subtitle">GitHub Copilot Customization Demo</p>

      {loading && <p className="loading">Loading tasks…</p>}
      {error && <p className="error">Error: {error}</p>}
      {!loading && !error && <TaskList tasks={tasks} />}
    </div>
  );
}

export default App;
