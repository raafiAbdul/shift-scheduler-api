package com.projects.shift_scheduler_api.models;

import java.util.Set;

public enum Role {
    ADMIN(Set.of(
            Permissions.SHIFT_READ,
            Permissions.SHIFT_WRITE,
            Permissions.SHIFT_UPDATE,
            Permissions.SHIFT_DELETE,
            Permissions.USER_READ,
            Permissions.USER_UPDATE_CONTACT,
            Permissions.WORKER_UPDATE_NONCONTACT,
            Permissions.MANAGER_UPDATE_NONCONTACT
    )),
    WORKER(Set.of(
            Permissions.SHIFT_READ,
            Permissions.SHIFT_CLAIM,
            Permissions.SHIFT_DROP,
            Permissions.USER_READ_SELF,
            Permissions.USER_READ,
            Permissions.USER_UPDATE_CONTACT,
            Permissions.CLOCK_IN,
            Permissions.CLOCK_OUT
    )),
    MANAGER(Set.of(
            Permissions.SHIFT_DROP,
            Permissions.SHIFT_READ,
            Permissions.SHIFT_WRITE,
            Permissions.SHIFT_UPDATE,
            Permissions.SHIFT_CLAIM,
            Permissions.SHIFT_DELETE,
            Permissions.USER_READ,
            Permissions.USER_READ_SELF,
            Permissions.WORKER_UPDATE_NONCONTACT,
            Permissions.USER_UPDATE_CONTACT,
            Permissions.CLOCK_IN,
            Permissions.CLOCK_OUT
    ));

    private final Set<Permissions> permissions;

    Role(Set<Permissions> permissions) {
        this.permissions = permissions;
    }

    public Set<Permissions> getPermissions() {
        return permissions;
    }
}
