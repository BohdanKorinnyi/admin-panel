package com.omnia.admin.model;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

public class CurrentUser extends org.springframework.security.core.userdetails.User {

    public CurrentUser(User user) {
        super(user.getUsername(), user.getPassword(),
                createAuthorityList(Role.parse(user.getRoleId()).toString())
        );
    }
}
