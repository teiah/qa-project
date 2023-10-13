package com.telerikacademy.testframework.utils;

public enum UserRoles {
    ROLE_ADMIN, ROLE_USER;

    private static final String ROLE_ERROR = "Role can be \"ROLE_ADMIN\" or \"ROLE_USER\".";
    @Override
    public String toString() {
        switch (this) {
            case ROLE_ADMIN:
                return "ROLE_ADMIN";
            case ROLE_USER:
                return "ROLE_USER";
            default:
                throw new IllegalArgumentException(ROLE_ERROR);
        }
    }
}
