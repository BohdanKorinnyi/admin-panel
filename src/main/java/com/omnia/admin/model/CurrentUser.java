package com.omnia.admin.model;

import lombok.Getter;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

@Getter
public class CurrentUser extends org.springframework.security.core.userdetails.User {
    private Integer buyerId;
    public CurrentUser(User user) {
        super(user.getUsername(), user.getPassword(),
                createAuthorityList(Role.parse(user.getRoleId()).toString())
        );
        this.buyerId = user.getBuyerId();
    }
}
