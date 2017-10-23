package com.omnia.admin.grid.dto.conversion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConversionGridFilterDetails {
    private int page;
    private int size;
    private ConversionFilterDto filter;
    private ConversionGridSortingDetails sortingDetails;

    public int getFirstElement() {
        return (page - 1) * size;
    }

    public int getSize() {
        return size;
    }
}
