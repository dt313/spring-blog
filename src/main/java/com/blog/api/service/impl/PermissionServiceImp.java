package com.blog.api.service.impl;

import com.blog.api.dto.request.PermissionRequest;
import com.blog.api.dto.response.PermissionResponse;
import com.blog.api.entities.Permission;
import com.blog.api.exception.AppException;
import com.blog.api.exception.ErrorCode;
import com.blog.api.mapper.PermissionMapper;
import com.blog.api.repository.PermissionRepository;
import com.blog.api.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImp implements PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;


    @Override
    public PermissionResponse create(PermissionRequest request) {
        var isExist = permissionRepository.existsById(request.getName());
        if(isExist) {
            throw new AppException(ErrorCode.PERMISSION_EXIST);
        }

        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    public List<PermissionResponse> getAll() {
        List<Permission> permissions = permissionRepository.findAll();

        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();

    }

    @Override
    public void delete(String name) {
        Permission permission = permissionRepository.findById(name)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));

        if(Objects.nonNull(permission)) {
            permissionRepository.deleteById(name);
        }
    }
}
