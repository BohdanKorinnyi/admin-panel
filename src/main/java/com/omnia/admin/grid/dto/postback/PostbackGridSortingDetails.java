package com.omnia.admin.grid.dto.postback;

import com.omnia.admin.grid.enums.SortOrder;
import lombok.Getter;
import lombok.Setter;

import static java.util.Objects.nonNull;

@Getter
@Setter
public class PostbackGridSortingDetails {
    private PostbackGridColumn column;
    private SortOrder order;

    public boolean hasSortingDetails() {
        return nonNull(column) && nonNull(order);
    }
}
