package com.omnia.admin.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BuyerKpi {
    private int id;
    private int buyerId;
    private LocalDate date;
    private int kpiName;
    private float kpiValue;
}
