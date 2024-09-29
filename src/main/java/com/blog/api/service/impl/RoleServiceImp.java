package com.blog.api.service.impl;

import com.blog.api.dto.request.RoleRequest;
import com.blog.api.dto.response.RoleResponse;
import com.blog.api.entities.Role;
import com.blog.api.exception.AppException;
import com.blog.api.exception.ErrorCode;
import com.blog.api.mapper.RoleMapper;
import com.blog.api.repository.PermissionRepository;
import com.blog.api.repository.RoleRepository;
import com.blog.api.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImp implements RoleService {

    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    @Override
    public RoleResponse create(RoleRequest request) {
        var isExist = roleRepository.existsById(request.getName());
        if(isExist) {
                throw new AppException(ErrorCode.ROLE_EXIST);
        }

        var permissions = permissionRepository.findAllById(request.getPermissions());
        Role role = roleMapper.toRole(request);
        role.setPermissions(new HashSet<>(permissions));

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    @Override
    public List<RoleResponse> getAll() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toRoleResponse).toList();

    }

    @Override
    public void delete(String name) {
        var isExist = roleRepository.existsById(name);
        if(isExist) {
            roleRepository.deleteById(name);
        }
        else {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }
    }
}
