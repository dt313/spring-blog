package com.blog.api.controller;

import com.blog.api.dto.request.PermissionRequest;
import com.blog.api.dto.response.PermissionResponse;
import com.blog.api.entities.ResponseObject;
import com.blog.api.service.PermissionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/permission")
public class PermissionController {

    PermissionService permissionService;

    @PostMapping("")
    ResponseEntity<ResponseObject> create(@RequestBody @Valid PermissionRequest request) {
        PermissionResponse result = permissionService.create(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK,"success",result));
    }

    @GetMapping("")
    ResponseEntity<ResponseObject> getAll() {
       List<PermissionResponse> result = permissionService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK,"success",result));
    }

    @DeleteMapping("/{permission}")
    ResponseEntity<ResponseObject> delete(@PathVariable String permission) {
        permissionService.delete(permission);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK,"success",null));
    }


}
