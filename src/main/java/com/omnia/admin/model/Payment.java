package com.omnia.admin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Payment {
    private int buyerId;
    private int staffId;
    private String month;
    private float sum;
    private String date;
    private String datePayroll;
    private String code;
    private int currencyId;
    private int typeId;
    private int walletId;
    private int payrollId;
    private String wallet;
}
