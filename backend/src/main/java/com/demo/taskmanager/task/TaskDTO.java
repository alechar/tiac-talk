package com.demo.taskmanager.task;

import java.time.LocalDateTime;

/**
 * DTO representing a task returned to API clients.
 */
public record TaskDTO(
        Long id,
        String title,
        String description,
        String priority,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    /** Maps a {@link Task} entity to a {@link TaskDTO}. */
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

    /** Converts this DTO to a {@link Task} entity (id and timestamps are managed by JPA). */
    public Task toEntity() {
        Task task = new Task();
        task.setTitle(this.title());
        task.setDescription(this.description());
        task.setPriority(this.priority());
        task.setStatus(this.status());
        return task;
    }
}
