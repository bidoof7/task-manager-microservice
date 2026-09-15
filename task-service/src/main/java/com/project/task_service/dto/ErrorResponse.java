package com.project.task_service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ErrorResponse(
        int status,
        String code,
        String message,
        String path,
        LocalDateTime timestamp
) {
}
