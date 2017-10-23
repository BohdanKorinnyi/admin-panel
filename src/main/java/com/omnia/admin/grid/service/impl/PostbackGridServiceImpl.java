package com.omnia.admin.grid.service.impl;

import com.omnia.admin.grid.dao.PostbackGridDao;
import com.omnia.admin.grid.dto.postback.PostbackGridFilterDetails;
import com.omnia.admin.grid.dto.postback.PostbackList;
import com.omnia.admin.grid.service.PostbackGridFilterQueryBuilder;
import com.omnia.admin.grid.service.PostbackGridService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostbackGridServiceImpl implements PostbackGridService {
    private final PostbackGridFilterQueryBuilder postbackGridFilterQueryBuilder;
    private final PostbackGridDao postbackGridDao;

    @Override
    public PostbackList getPostbackList(PostbackGridFilterDetails filterDetails) {
        String filterQuery = postbackGridFilterQueryBuilder.createFilterQuery(filterDetails);
        return postbackGridDao.getPostbacks(filterDetails, filterQuery);
    }
}
