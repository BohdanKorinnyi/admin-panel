package com.omnia.admin.model.statistic;

import com.omnia.admin.model.BuyerProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Stats {
    private BuyerProjection buyerInfo;
    private SourcesResult sources;      // statistic = sources => spent
    private ExpensesResult expenses;    // spent
    private PostbackResult postbacks;   // revenue
}
