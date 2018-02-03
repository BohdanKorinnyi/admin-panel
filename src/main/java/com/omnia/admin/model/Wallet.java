package com.omnia.admin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Wallet {
    private Long id;
    private Long buyerId;
    private Long staffId;
    private String type;
    private String number;
    private Long currencyId;
    private String description;
}
