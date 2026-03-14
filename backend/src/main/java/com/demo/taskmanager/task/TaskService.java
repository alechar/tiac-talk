package com.demo.taskmanager.task;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(TaskDTO::fromEntity)
                .collect(Collectors.toList()); // OLD STYLE: could use .toList()
    }

    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElse(null); // OLD STYLE: returns null instead of Optional
        if (task == null) {
            return null;
        }
        return TaskDTO.fromEntity(task);
    }

    public TaskDTO createTask(TaskDTO dto) {
        Task task = dto.toEntity();
        Task saved = taskRepository.save(task);
        return TaskDTO.fromEntity(saved);
    }

    public TaskDTO updateTask(Long id, TaskDTO dto) {
        Task existing = taskRepository.findById(id).orElse(null);
        if (existing == null) {
            return null; // OLD STYLE: null instead of exception or Optional
        }
        existing.setTitle(dto.title());
        existing.setDescription(dto.description());
        existing.setPriority(dto.priority());
        existing.setStatus(dto.status());
        Task saved = taskRepository.save(existing);
        return TaskDTO.fromEntity(saved);
    }

    public boolean deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            return false;
        }
        taskRepository.deleteById(id);
        return true;
    }
}
