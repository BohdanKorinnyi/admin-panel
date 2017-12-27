package com.omnia.admin.service.impl;

import com.omnia.admin.dao.AdvertiserDao;
import com.omnia.admin.dao.AdvertsIncomeDao;
import com.omnia.admin.dto.AdvertiserDto;
import com.omnia.admin.model.Advertiser;
import com.omnia.admin.service.AdvertiserService;
import com.omnia.admin.service.AdvertiserStatusService;
import com.omnia.admin.service.PostbackService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class AdvertiserServiceImpl implements AdvertiserService {
    private final AdvertiserDao advertiserDao;
    private final PostbackService postbackService;
    private final AdvertsIncomeDao advertsIncomeDao;
    private final AdvertiserStatusService advertiserStatusService;

    @Override
    public List<Advertiser> getAllAdvertisers() {
        return advertiserDao.findAll();
    }

    @Override
    public List<String> getAdvertisersName() {
        return advertiserDao.getAllAdvertisersName();
    }

    @Override
    public void update(List<AdvertiserDto> advertisers) {
        for (AdvertiserDto advertiser : advertisers) {
            if (isNull(advertiser.getId())) {
                Long advertiserId = (Long) advertiserDao.save(advertiser);
                advertiserStatusService.save(advertiser.getStatuses(), advertiserId);
            } else {
                advertiserDao.update(advertiser);
                advertiserStatusService.update(advertiser.getStatuses(), advertiser.getId());
            }
        }
    }

    @Override
    public Object report(List<Integer> advertiserIds, String from, String to) {
        Map<String, Object> result = new HashMap<>();
        result.put("totalRevenue", postbackService.getRevenueByPeriod(advertiserIds, from, to));
        result.put("totalIncome", advertsIncomeDao.getTotalByPeriod(advertiserIds, from, to));
        result.put("incomes", advertsIncomeDao.getIncomeByPeriodGroupedByAdvertiser(advertiserIds, from, to));
        return result;
    }
}
