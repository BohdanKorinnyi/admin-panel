package com.omnia.admin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Source {
    private int amount;
    private Double spent;
    private Integer buyerId;
    private String campaignName;
    private String accountType;
    private String username;
    private String date;
}
