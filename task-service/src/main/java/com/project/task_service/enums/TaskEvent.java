package com.project.task_service.enums;

public enum TaskEvent {
    TASK_CREATED("TaskCreated"),
    TASK_UPDATED("TaskUpdated"),
    TASK_DELETED("TaskDeleted");

    private final String taskStatus;

    TaskEvent(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskStatus() {
        return this.taskStatus;
    }
}
