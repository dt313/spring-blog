package com.blog.api.service;

import com.blog.api.dto.request.RoleRequest;
import com.blog.api.dto.response.RoleResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {
    RoleResponse create(RoleRequest request);

    List<RoleResponse> getAll();
    void delete(String name);

}
