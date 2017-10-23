package com.omnia.admin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class Exchange {
    private Long id;
    private Date time;
    private Long count;
    private Long currencyId;
    private Double rate;
}
