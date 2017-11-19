package com.omnia.admin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

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

    @JsonIgnore
    public LocalDate getLocalDate() {
        return LocalDate.parse(date);
    }
}
