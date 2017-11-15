package com.omnia.admin.model.statistic;

import com.omnia.admin.model.Expenses;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExpensesResult extends BuyerDetails {
    private List<Expenses> data;
}
