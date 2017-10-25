package com.omnia.admin.model;

import com.omnia.admin.exception.UnknownRoleException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    BUYER(1),
    TEAM_LEADER(2),
    CBO(3),//Media Buyer Director
    MENTOR(4),
    CFO(5),//Chief Finance Office
    DIRECTOR(6),
    ADMIN(7);

    private int value;

    public static Role parse(int value) {
        for (Role role : Role.values()) {
            if (role.getValue() == value) {
                return role;
            }
        }
        throw new UnknownRoleException();
    }
}
