package com.omnia.admin.dao;

import com.omnia.admin.model.Balance;
import org.springframework.util.CollectionUtils;

import java.util.List;

public interface BalanceDao {
    List<Balance> getMonthBalance(int year, String month, List<Long> advertiserIds);

    default List<Long> nullIfEmpty(List<Long> values) {
        return CollectionUtils.isEmpty(values) ? null : values;
    }
}
