package com.blog.api.mapper;

import com.blog.api.dto.request.PermissionRequest;
import com.blog.api.dto.response.PermissionResponse;
import com.blog.api.entities.Permission;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);

}
