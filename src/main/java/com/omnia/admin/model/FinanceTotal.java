package com.omnia.admin.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FinanceTotal {
    private Float revenue;
    private Float spent;
    private Float profit;
    private String day;
    private LocalDate date;
    private String buyer;
}
