package com.omnia.admin.dao;

import java.util.List;

public interface ProcessedPostbackDao {
    boolean isPostbackProcessed(String postbackId);

    void save(List<String> postbackKeys);
}
