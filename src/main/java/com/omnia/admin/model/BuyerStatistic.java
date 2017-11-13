package com.omnia.admin.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BuyerStatistic {
    private Integer buyerId;
    private String buyerName;
    private Double buyerTotalSpent;
    private List<SourceStatistic> sourceStatistics;
}
