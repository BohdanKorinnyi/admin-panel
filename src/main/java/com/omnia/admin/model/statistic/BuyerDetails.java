package com.omnia.admin.model.statistic;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BuyerDetails {
    protected Integer id;
    protected String name;
    protected Double sum;
}
