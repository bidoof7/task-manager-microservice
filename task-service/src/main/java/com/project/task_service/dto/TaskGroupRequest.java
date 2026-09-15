package com.project.task_service.dto;

import jakarta.validation.constraints.NotBlank;

public record TaskGroupRequest(
        @NotBlank String name
) {
}
