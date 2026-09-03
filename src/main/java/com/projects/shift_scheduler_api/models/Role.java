package com.projects.shift_scheduler_api.models;

import java.util.Set;

public enum Role {
    ADMIN(Set.of(
            Permissions.SHIFT_READ,
            Permissions.SHIFT_WRITE,
            Permissions.SHIFT_UPDATE,
            Permissions.SHIFT_DELETE,
            Permissions.USER_READ,
            Permissions.USER_WRITE,
            Permissions.USER_UPDATE,
            Permissions.USER_DELETE
    )),
    WORKER(Set.of(
            Permissions.SHIFT_READ,
            Permissions.SHIFT_CLAIM,
            Permissions.SHIFT_SWAP_REQUEST,
            Permissions.USER_READ_SELF,
            Permissions.USES_UPDATE_CONTACT
    )),
    MANAGER(Set.of(
            Permissions.SHIFT_READ,
            Permissions.SHIFT_WRITE,
            Permissions.SHIFT_UPDATE,
            Permissions.SHIFT_DELETE,
            Permissions.USER_READ,
            Permissions.USER_UPDATE_NONCONTACT
    ));

    private final Set<Permissions> permissions;

    Role(Set<Permissions> permissions) {
        this.permissions = permissions;
    }

    public Set<Permissions> getPermissions() {
        return permissions;
    }
}
