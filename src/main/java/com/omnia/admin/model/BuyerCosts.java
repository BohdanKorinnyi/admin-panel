package com.omnia.admin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyerCosts {
    private float spent;
    private int buyerId;
    private String date;
    private String source;
    private String buyerName;
}
