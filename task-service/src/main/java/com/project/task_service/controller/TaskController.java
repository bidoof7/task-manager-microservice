package com.project.task_service.controller;

import com.project.task_service.dto.Response;
import com.project.task_service.dto.TaskRequest;
import com.project.task_service.entity.Task;
import com.project.task_service.service.TaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@Tag(
        name = "Task management",
        description = "Task management APIs"
)
@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService _taskService;

    @GetMapping
    public ResponseEntity<Response<List<Task>>> findAll() {
        return ResponseEntity.ok(new Response<>(true, "ALL TASKS", _taskService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Task>> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(new Response<>(true, "TASK ID " + id + " had found", _taskService.findById(id)));
    }

    @GetMapping(params = "title")
    public ResponseEntity<Response<List<Task>>> searchTitle(@RequestParam String title) {
        return ResponseEntity.ok(new Response<>(true, "ALL TASKS WITH TITLE " + title, _taskService.searchByTitle(title)));
    }

    @PostMapping
    public ResponseEntity<Response<Task>> save(@Valid @RequestBody TaskRequest request) {
        Task savedTask = _taskService.save(request);
        return ResponseEntity.ok(new Response<>(true, "SAVE NEW TASK SUCCESSFULLY", savedTask));
    }

    @PutMapping("{id}")
    public ResponseEntity<Response<Task>> update(@Valid @RequestBody TaskRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(new Response<>(true, "TASK ID " + id + " UPDATED", _taskService.update(id, request)));
    }

//    @DeleteMapping("{id}")
//    public ResponseEntity<Response<Void>> delete(@PathVariable Long id) {
//        _taskService.delete(id);
//        return ResponseEntity.ok(new Response<>(true, "TASK ID " + id + " DELETED", null));
//    }
}
