package com.blog.api.dto.principal;

import lombok.Builder;
import lombok.Data;

import java.security.Principal;

public class PrincipalUser implements Principal {
    private String userId;
    public PrincipalUser(String userId) {
        this.userId = userId;
    }
    @Override
    public String getName() {
        return userId;
    }
}
