package com.omnia.admin.service.impl;

import com.omnia.admin.dao.PostbackDao;
import com.omnia.admin.model.Postback;
import com.omnia.admin.service.PostbackService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostbackServiceImpl implements PostbackService {
    private final PostbackDao postbackDao;

    @Override
    public Optional<String> getFullUrl(Long postbackId) {
        return postbackDao.findFullUrlById(postbackId);
    }

    @Override
    public Float getRevenueByBuyer(int buyerId) {
        return postbackDao.getRevenueByBuyer(buyerId);
    }

    @Override
    public List<Postback> getPostbacksByConversionId(long conversionId) {
        return postbackDao.findPostbackByConversionId(conversionId);
    }
}
