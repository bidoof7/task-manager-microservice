package com.project.task_service.service;

import com.project.task_service.entity.Task;
import com.project.task_service.dto.TaskRequest;
import com.project.task_service.event.TaskCreatedEvent;
import com.project.task_service.exception.TaskNotFound;
import com.project.task_service.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final OutboxEventService outboxEventService;

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assert authentication != null;
        String username = authentication.getName();

        boolean isAdmin = authentication
                .getAuthorities()
                .stream()
                .anyMatch(
                        authority -> Objects.equals(authority.getAuthority(), "ROLE_ADMIN")
                );

        if (!isAdmin && !task.getCreatedBy().equals(username)) {
            throw new AccessDeniedException("USER IS NOT AVAILABLE TO UPDATE THIS TASK");
        }

        task.setTitle(request.title());
        task.setDescription(request.description());

        Task updatedTask = taskRepository.save(task);

        outboxEventService.saveTaskUpdatedEvent(updatedTask);

        return updatedTask;
    }

//    @PreAuthorize("hasAuthority('TASK_DELETE')")
//    public void delete(Long id)  {
//        String username = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
//
//        Task task = _taskRepository.findById(id).orElseThrow(() -> new TaskNotFound("TASK ID " + id + " NOT EXIST"));
//
//        if(!task.getCreatedBy().equals(username)) {
//            throw new AccessDeniedException("DONT HAVE PERMISSION");
//        }
//
//        _taskRepository.deleteById(id);
//    }
}
