package com.omnia.admin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Payment {
    private String month;
    private float sum;
    private String date;
    private String code;
}
