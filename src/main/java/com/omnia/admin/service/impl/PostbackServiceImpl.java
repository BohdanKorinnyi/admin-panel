package com.omnia.admin.service.impl;

import com.omnia.admin.dao.PostbackDao;
import com.omnia.admin.service.PostbackService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PostbackServiceImpl implements PostbackService {

    private final PostbackDao postbackDao;

    @Override
    public Optional<String> getFullUrl(Long postbackId) {
        return postbackDao.findFullUrlById(postbackId);
    }
}
