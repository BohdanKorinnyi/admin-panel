package com.omnia.admin.grid.service;

import com.omnia.admin.grid.dto.postback.PostbackGridFilterDetails;
import com.omnia.admin.grid.dto.postback.PostbackList;

public interface PostbackGridService {
    PostbackList getPostbackList(PostbackGridFilterDetails filterDetails);
}
