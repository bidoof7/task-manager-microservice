package com.project.task_service.service;

import com.project.task_service.entity.Task;
import com.project.task_service.dto.TaskRequest;
import com.project.task_service.entity.TaskGroup;
import com.project.task_service.exception.TaskGroupNotFound;
import com.project.task_service.exception.TaskNotFound;
import com.project.task_service.repository.TaskRepository;
import com.project.task_service.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskGroupService taskGroupService;
    private final OutboxEventService outboxEventService;
    private final SecurityUtils securityUtils;

    @PreAuthorize("hasAuthority('TASK_READ')")
    public Page<Task> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @PreAuthorize("hasAuthority('TASK_READ')")
    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFound(id));
    }

    @PreAuthorize("hasAuthority('TASK_READ')")
    public List<Task> searchByTitle(String title) {
        if (title.isEmpty()) {
            throw new RuntimeException("TITLE CANT BE NULL");
        }
        return taskRepository.findByTitleContaining(title);
    }

    @PreAuthorize("hasAuthority('TASK_CREATE')")
    @Transactional
    public Task save(TaskRequest request) {
        TaskGroup groupId = taskGroupService.findById(request.groupId());

        Task task = new Task();

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setGroupId(groupId);

        Task savedTask = taskRepository.save(task);

        outboxEventService.saveTaskCreatedEvent(savedTask);

        return savedTask;
    }

    @PreAuthorize("hasAuthority('TASK_UPDATE')")
    @Transactional
    public Task update(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFound(id));

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
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFound(id));

        String username = securityUtils.getCurrentUsername();
        boolean isAdmin = securityUtils.isAdmin();

        if (!isAdmin && !task.getCreatedBy().equals(username)) {
            throw new AccessDeniedException("USER IS NOT AVAILABLE TO UPDATE THIS TASK");
        }

        outboxEventService.saveTaskDeletedEvent(task);

        taskRepository.deleteById(id);
    }
}
