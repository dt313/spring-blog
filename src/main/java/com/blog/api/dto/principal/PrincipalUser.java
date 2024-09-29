package com.blog.api.dto.principal;

import java.security.Principal;

public class PrincipalUser implements Principal {
    private final String userId;
    public PrincipalUser(String userId) {
        this.userId = userId;
    }
    @Override
    public String getName() {
        return userId;
    }
}
