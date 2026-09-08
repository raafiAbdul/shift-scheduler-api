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
            Permissions.USER_DELETE,
            Permissions.USER_UPDATE_CONTACT,
            Permissions.USER_READ_SELF,
            Permissions.WORKER_UPDATE_NONCONTACT,
            Permissions.MANAGER_UPDATE_NONCONTACT,
            Permissions.SHIFT_CLAIM,
            Permissions.SHIFT_SWAP_REQUEST,
            Permissions.USER_READ_BASIC_DETAILS
    )),
    WORKER(Set.of(
            Permissions.SHIFT_READ,
            Permissions.SHIFT_CLAIM,
            Permissions.SHIFT_SWAP_REQUEST,
            Permissions.USER_READ_SELF,
            Permissions.USER_UPDATE_CONTACT,
            Permissions.USER_READ_BASIC_DETAILS
    )),
    MANAGER(Set.of(
            Permissions.SHIFT_READ,
            Permissions.SHIFT_WRITE,
            Permissions.SHIFT_UPDATE,
            Permissions.SHIFT_DELETE,
            Permissions.USER_READ,
            Permissions.WORKER_UPDATE_NONCONTACT,
            Permissions.USER_READ_BASIC_DETAILS
    ));

    private final Set<Permissions> permissions;

    Role(Set<Permissions> permissions) {
        this.permissions = permissions;
    }

    public Set<Permissions> getPermissions() {
        return permissions;
    }
}
