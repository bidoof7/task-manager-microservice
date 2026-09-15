package com.project.task_service.controller;

import com.project.task_service.dto.Response;
import com.project.task_service.dto.TaskGroupRequest;
import com.project.task_service.entity.TaskGroup;
import com.project.task_service.service.TaskGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/task-group")
@RequiredArgsConstructor
public class TaskGroupController {
    private final TaskGroupService taskGroupService;

    @PostMapping
    public ResponseEntity<Response<TaskGroup>> saveGroupTask(@Valid @RequestBody TaskGroupRequest body) {
        return ResponseEntity.ok(new Response<>(true, "TASK GROUP CREATE SUCCESSFULLY",taskGroupService.save(body)));
    }
}
