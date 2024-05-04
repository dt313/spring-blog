package com.blog.api.controller;

import com.blog.api.dto.request.RoleRequest;
import com.blog.api.dto.response.RoleResponse;
import com.blog.api.entities.ResponseObject;
import com.blog.api.service.RoleService;
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
@RequestMapping("/api/v1/roles")
public class RoleController {

    RoleService roleService;

    @PostMapping("")
    ResponseEntity<ResponseObject> create(@RequestBody @Valid RoleRequest request) {
        RoleResponse result = roleService.create(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK,"success",result));
    }

    @GetMapping("")
    ResponseEntity<ResponseObject> getAll() {
       List<RoleResponse> result = roleService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK,"success",result));
    }

    @DeleteMapping("/{role}")
    ResponseEntity<ResponseObject> delete(@PathVariable String role) {
        roleService.delete(role);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK,"success",null));
    }


}
