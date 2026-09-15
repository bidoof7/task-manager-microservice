package com.project.task_service.service;

import com.project.task_service.dto.TaskGroupRequest;
import com.project.task_service.entity.TaskGroup;
import com.project.task_service.exception.TaskGroupNotFound;
import com.project.task_service.repository.TaskGroupRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskGroupService {
    private final TaskGroupRepository taskGroupRepository;

    @PreAuthorize("hasAuthority('TASK_CREATE')")
    @Transactional
    public TaskGroup save(TaskGroupRequest request) {
        TaskGroup taskGroup = new TaskGroup();

        taskGroup.setName(request.name());

        return taskGroupRepository.save(taskGroup);
    }

    public TaskGroup findById(Long id) {
        return taskGroupRepository.findById(id).orElseThrow(() -> new TaskGroupNotFound(id));
    }
}
