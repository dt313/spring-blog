package com.blog.api.service;

import com.blog.api.dto.request.PermissionRequest;
import com.blog.api.dto.response.PermissionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PermissionService {
    public PermissionResponse create(PermissionRequest request);
    public List<PermissionResponse> getAll();
    public void delete(String name);
}
