package com.omnia.admin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.springframework.util.StringUtils.isEmpty;

@Getter
@AllArgsConstructor
public class ColumnOrder {
    private final String column;
    private final String order;

    public boolean isValid() {
        return !isEmpty(column) && !isEmpty(order);
    }
}
