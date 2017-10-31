package com.omnia.admin.grid;

import com.omnia.admin.model.ColumnOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.omnia.admin.grid.filter.FilterConstant.LIMIT;

@AllArgsConstructor
public class Page {
    private Integer size;
    @Getter
    private Integer number;
    @Getter
    private ColumnOrder columnOrder;

    public String limit() {
        return String.format(LIMIT, (number - 1) * size, size);
    }
}
