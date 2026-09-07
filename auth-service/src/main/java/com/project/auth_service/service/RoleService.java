package com.project.auth_service.service;

import com.project.auth_service.entity.Role;
import com.project.auth_service.exception.RoleNotFound;
import com.project.auth_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role findByName(String name) {
        return roleRepository.findByRoleName(name).orElseThrow(() -> new RoleNotFound("ROLE NOT FOUND"));
    }
}
