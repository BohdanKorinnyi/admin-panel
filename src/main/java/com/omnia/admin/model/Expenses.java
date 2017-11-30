package com.omnia.admin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Expenses {
    private Long id;
    private Integer buyerId;
    private String buyer;
    private String date;
    private Float sum;
    private String name;
}
