package com.omnia.admin.grid.dao;

import com.omnia.admin.grid.dto.postback.PostbackGridFilterDetails;
import com.omnia.admin.grid.dto.postback.PostbackList;

public interface PostbackGridDao {
    PostbackList getPostbacks(PostbackGridFilterDetails filterDetails, String whereQuery);
}
