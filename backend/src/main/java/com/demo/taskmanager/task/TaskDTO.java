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

    /** Creates a TaskDTO from a {@link Task} entity. */
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

    /** Converts this DTO to a new {@link Task} entity (without id or timestamps). */
    public Task toEntity() {
        Task task = new Task();
        task.setTitle(this.title());
        task.setDescription(this.description());
        task.setPriority(this.priority());
        task.setStatus(this.status());
        return task;
    }
}
