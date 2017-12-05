package com.omnia.admin.model;

import com.omnia.admin.exception.UnknownRoleException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Log4j
@Getter
@AllArgsConstructor
public enum Role {
    BUYER("Buyer"),
    TEAM_LEADER("Team Leader"),
    CBO("CBO"),
    MENTOR("Mentor"),
    CFO("CFO"),
    DIRECTOR("Director"),
    ADMIN("Administrator");

    private String value;

    public static Role parse(String value) {
        for (Role role : Role.values()) {
            if (role.getValue().equalsIgnoreCase(value)) {
                return role;
            }
        }
        log.error("UnknownRole=" + value);
        throw new UnknownRoleException();
    }
}
