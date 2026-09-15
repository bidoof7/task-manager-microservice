package com.project.task_service.exception;

public class TaskGroupNotFound extends RuntimeException {
    private final Long groupId;

    public TaskGroupNotFound(Long groupId) {
        super("Task group with ID: " + groupId + " was not foud");
        this.groupId = groupId;
    }

    public Long getGroupId() {
        return groupId;
    }
}
