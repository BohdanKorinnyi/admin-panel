package com.omnia.admin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdvertiserIncome {
    private int id;
    private String advertiser;
    private Float revenue;
    private Float income;
    private Float liability;

    private Float getLiability() {
        return revenue - income;
    }
}
