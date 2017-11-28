package com.omnia.admin.service;

import java.util.Optional;

public interface PostbackService {
    Optional<String> getFullUrl(Long postbackId);

    Float getRevenueByBuyer(int buyerId);
}
