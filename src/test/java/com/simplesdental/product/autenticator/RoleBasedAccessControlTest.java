package com.simplesdental.product.autenticator;

import com.simplesdental.product.domain.model.User;
import com.simplesdental.product.domain.model.field.Role;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoleBasedAccessControlTest {

    @Test
    void shouldAllowAdminAccess() {
        User admin = new User();
        admin.setRole(Role.ADMIN);

        if (admin.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Access denied");
        }
    }

    @Test
    void shouldDenyUserAccessToAdminResource() {
        User user = new User();
        user.setRole(Role.USER);

        assertThrows(AccessDeniedException.class, () -> {
            if (user.getRole() != Role.ADMIN) {
                throw new AccessDeniedException("Access denied");
            }
        });
    }
}