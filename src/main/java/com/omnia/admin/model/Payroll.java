package com.omnia.admin.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class Payroll {
    private Long id;
    private Long buyerId;
    private Date date;
    private String description;
    private Integer type;
    private Float sum;
    private Integer currencyId;
}
