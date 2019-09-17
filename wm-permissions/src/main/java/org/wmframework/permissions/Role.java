package org.wmframework.permissions;

import lombok.Getter;

@Getter
public enum Role {

    SUPER_ADMIN(0),

    ADMIN(1),

    NORMAL(2),
    ;

    private int code;

    Role(int code) {
        this.code = code;
    }
}
