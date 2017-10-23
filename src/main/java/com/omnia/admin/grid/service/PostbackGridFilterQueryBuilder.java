package com.omnia.admin.grid.service;

import com.omnia.admin.grid.dto.postback.PostbackGridFilterDetails;

public interface PostbackGridFilterQueryBuilder {
    String createFilterQuery(PostbackGridFilterDetails filterDetails);
}
