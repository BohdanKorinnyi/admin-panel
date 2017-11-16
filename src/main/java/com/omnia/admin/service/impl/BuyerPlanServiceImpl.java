package com.omnia.admin.service.impl;

import com.omnia.admin.service.BuyerPlanService;
import com.omnia.admin.service.PostbackService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class BuyerPlanServiceImpl implements BuyerPlanService {
    private final PostbackService postbackService;

    @Override
    public List<Object> getBuyerPlan(List<String> months, List<String> buyers) {
        return Collections.emptyList();
    }
}
