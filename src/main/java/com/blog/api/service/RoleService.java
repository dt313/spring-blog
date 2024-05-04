package com.blog.api.service;

import com.blog.api.dto.request.RoleRequest;
import com.blog.api.dto.response.RoleResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {
    public RoleResponse create(RoleRequest request);

    public List<RoleResponse> getAll();
    public void delete(String name);

}
