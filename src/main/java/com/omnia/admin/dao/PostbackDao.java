package com.omnia.admin.dao;

import java.util.Optional;

public interface PostbackDao {
    Optional<String> findFullUrlById(Long postbackId);
}
