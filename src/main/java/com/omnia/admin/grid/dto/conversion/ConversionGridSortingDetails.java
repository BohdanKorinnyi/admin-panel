package com.omnia.admin.grid.dto.conversion;

import com.omnia.admin.grid.enums.conversion.ConversionGridColumn;
import com.omnia.admin.grid.enums.SortOrder;
import lombok.Getter;
import lombok.Setter;

import static java.util.Objects.nonNull;

@Getter
@Setter
public class ConversionGridSortingDetails {
    private ConversionGridColumn column;
    private SortOrder order;

    public boolean hasSortingDetails() {
        return nonNull(column) && nonNull(order);
    }
}
