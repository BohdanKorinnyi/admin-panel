package com.omnia.admin.grid.dto.postback;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostbackGridFilterDetails {
    private int page;
    private int size;
    private PostbackFilterDto filter;
    private PostbackGridSortingDetails sortingDetails;

    public int getFirstElement() {
        return (page - 1) * size;
    }

    public int getSize() {
        return size;
    }
}
