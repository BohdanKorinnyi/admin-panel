package com.omnia.admin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SourceStat {
    private int quantity;
    private Float spent;
    private Float revenue;
    private String buyer;
    private Integer buyerId;
    private String date;
    private String type;
}
