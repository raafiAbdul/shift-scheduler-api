package com.projects.shift_scheduler_api.models;

public enum Permissions {
    SHIFT_READ,
    SHIFT_WRITE,
    SHIFT_UPDATE,
    SHIFT_DELETE,
    SHIFT_CLAIM,
    SHIFT_DROP,
    USER_READ,
    USER_UPDATE,
    USER_UPDATE_CONTACT,
    USER_READ_SELF,
    WORKER_UPDATE_NONCONTACT,
    MANAGER_UPDATE_NONCONTACT,
    CLOCK_IN,
    CLOCK_OUT
}
