package com.omnia.admin.service.impl;

import com.omnia.admin.dao.BuyerKpiDao;
import com.omnia.admin.service.BuyerKpiService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BuyerKpiServiceImpl implements BuyerKpiService {
    private final BuyerKpiDao buyerKpiDao;
}
