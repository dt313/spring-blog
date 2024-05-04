package com.blog.api.mapper;

import com.blog.api.dto.request.RoleRequest;
import com.blog.api.dto.response.RoleResponse;
import com.blog.api.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);

}
