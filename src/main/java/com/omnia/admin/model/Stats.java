package com.omnia.admin.model;

import com.omnia.admin.service.PostbackStats;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Stats {
    private BuyerProjection buyerInfo;
    private BuyerStatistic sources;
    private List<Expenses> expenses;
    private List<PostbackStats> postbacks;
}
