package com.project.task_service.service;

import com.project.task_service.entity.Task;
import com.project.task_service.dto.TaskRequest;
import com.project.task_service.exception.TaskNotFound;
import com.project.task_service.repository.TaskRepository;
import com.project.task_service.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final OutboxEventService outboxEventService;
    private final SecurityUtils securityUtils;

    @PreAuthorize("hasAuthority('TASK_READ')")
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @PreAuthorize("hasAuthority('TASK_READ')")
    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFound("TASK ID " + id + " NOT EXIST"));
    }

    @PreAuthorize("hasAuthority('TASK_READ')")
    public List<Task> searchByTitle(String title) {
        if (title.isEmpty()) {
            throw new TaskNotFound("TITLE CAN NOT EMPTY");
        }
        return taskRepository.findByTitleContaining(title);
    }

    @PreAuthorize("hasAuthority('TASK_CREATE')")
    @Transactional
    public Task save(TaskRequest request) {
        Task task = new Task();

        task.setTitle(request.title());
        task.setDescription(request.description());

        Task savedTask = taskRepository.save(task);

        outboxEventService.saveTaskCreatedEvent(savedTask);

        return savedTask;
    }

    @PreAuthorize("hasAuthority('TASK_UPDATE')")
    @Transactional
    public Task update(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFound("TASK ID " + id + " NOT EXIST"));

        String username = securityUtils.getCurrentUsername();
        boolean isAdmin = securityUtils.isAdmin();

        if (!isAdmin && !task.getCreatedBy().equals(username)) {
            throw new AccessDeniedException("USER IS NOT AVAILABLE TO UPDATE THIS TASK");
        }

        task.setTitle(request.title());
        task.setDescription(request.description());

        Task updatedTask = taskRepository.save(task);

        outboxEventService.saveTaskUpdatedEvent(updatedTask);

        return updatedTask;
    }

    @PreAuthorize("hasAuthority('TASK_DELETE')")
    @Transactional
    public void delete(Long id)  {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFound("TASK ID " + id + " NOT EXIST"));

        String username = securityUtils.getCurrentUsername();
        boolean isAdmin = securityUtils.isAdmin();

        if (!isAdmin && !task.getCreatedBy().equals(username)) {
            throw new AccessDeniedException("USER IS NOT AVAILABLE TO UPDATE THIS TASK");
        }

        outboxEventService.saveTaskDeletedEvent(task);

        taskRepository.deleteById(id);
    }
}
