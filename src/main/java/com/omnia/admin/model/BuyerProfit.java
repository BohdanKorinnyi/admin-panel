package com.omnia.admin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyerProfit {
    private int buyerId;
    private Float spent;
    private Float revenue;
    private Float profit;
    private String buyer;
    private String date;
    private String fullDate;
    private String day;
}
