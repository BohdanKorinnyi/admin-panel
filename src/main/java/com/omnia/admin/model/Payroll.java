package com.omnia.admin.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class Payroll {
    private Long id;
    private Long staffId;
    private Date date;
    private Date periond;
    private String description;
    private Integer typeId;
    private Float sum;
    private Float value;
    private Integer currencyId;
    private String currency;
    private String month;
}
