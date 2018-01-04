package com.omnia.admin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinanceAccount {
    private int id;
    private String name;
    private String type;
    private int currencyId;
    private String code;
}
