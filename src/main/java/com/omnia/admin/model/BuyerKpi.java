package com.omnia.admin.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class BuyerKpi {
    private int id;
    private int buyerId;
    private String date;
    private int kpiName;
    private float kpiValue;
    private float execution;
    private String kpiCatalogName;
}
