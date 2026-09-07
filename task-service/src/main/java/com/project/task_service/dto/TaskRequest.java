package com.project.task_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskRequest(
        @NotBlank @Size(max = 100, message = "Không thể vượt quá 100 từ") String title,
        @Size(max = 255, message = "Không thể vượt quá 255 từ") String description
) { }
