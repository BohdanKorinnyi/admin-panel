package com.omnia.admin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyerPlan {
    private String month;
    private String buyerName;
    private String kpiName;
    private Float kpiValue;
    private Float sum;
    private String currency;
    private Float performance;
}
