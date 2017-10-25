package com.omnia.admin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
    private Integer userId;
    private String username;
    private String name;
    private String firstName;
    private String secodName;
    @JsonIgnore
    private String password;

    private Integer roleId;
    private Integer groupId;
    private Integer buyerId;
}
