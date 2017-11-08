package com.omnia.admin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {
    private long accountId;
    private long buyerId;
    private int actual;
    private String username;
    private String type;
    private String name;
    private String owner;
}
