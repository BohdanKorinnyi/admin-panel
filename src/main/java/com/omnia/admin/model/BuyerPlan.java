package com.omnia.admin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.Month;

@Getter
@Setter
public class BuyerPlan {
    private Integer kpiId;
    @JsonIgnore
    private Integer month;
    private String buyerName;
    private String kpiName;
    private Float kpiValue;
    private Float sum;
    private String currency;
    private Float execution;

    public String getMonthName() {
        return Month.of(month).toString();
    }
}
