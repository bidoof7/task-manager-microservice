package com.project.task_service.exception;

public class TaskNotFound extends RuntimeException{
    private final Long taskId;
    public TaskNotFound(Long taskId) {
        super("TASK ID " + taskId + " NOT EXIST");
        this.taskId = taskId;
    }

    public Long getTaskId() {
        return taskId;
    }
}
