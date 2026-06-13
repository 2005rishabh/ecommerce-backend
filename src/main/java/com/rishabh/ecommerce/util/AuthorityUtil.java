package com.rishabh.ecommerce.util;

import com.rishabh.ecommerce.entities.Role;

public final class AuthorityUtil {

    private AuthorityUtil() {
    }

    public static String toAuthority(Role role) {
        return "ROLE_" + role.name();
    }
}
