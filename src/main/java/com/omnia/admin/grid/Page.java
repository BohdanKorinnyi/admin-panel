package com.omnia.admin.grid;

import lombok.AllArgsConstructor;

import static com.omnia.admin.grid.filter.FilterConstant.LIMIT;

@AllArgsConstructor
public class Page {
    private Integer size;
    private Integer number;

    public String limit() {
        return String.format(LIMIT, (size - 1) * number, size);
    }
}
