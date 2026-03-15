package com.demo.taskmanager.task;

import java.time.LocalDateTime;

public record TaskDTO(
        Long id,
        String title,
        String description,
        String priority,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    // --- Mapping helper ---
    public static TaskDTO fromEntity(Task task) {
        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    public Task toEntity() {
        Task task = new Task();
        task.setTitle(this.title());
        task.setDescription(this.description());
        task.setPriority(this.priority());
        task.setStatus(this.status());
        return task;
    }
}
