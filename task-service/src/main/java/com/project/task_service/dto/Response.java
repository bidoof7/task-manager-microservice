package com.project.task_service.dto;

public record Response<T>(boolean status, String message, T data) {}
