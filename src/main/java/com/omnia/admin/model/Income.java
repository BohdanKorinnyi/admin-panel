package com.omnia.admin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Income {
    private String date;
    private Double total;
    private Double commission;
    private Double bank;
    private Long accountId;
    private Long advertiserId;
    private Long currencyId;
}
