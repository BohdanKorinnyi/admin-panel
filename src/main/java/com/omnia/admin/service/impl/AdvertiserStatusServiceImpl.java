package com.omnia.admin.service.impl;

import com.omnia.admin.dao.AdvertiserStatusDao;
import com.omnia.admin.dto.AdvertiserStatusDto;
import com.omnia.admin.model.AdvertiserStatus;
import com.omnia.admin.service.AdvertiserStatusService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class AdvertiserStatusServiceImpl implements AdvertiserStatusService {

    private final AdvertiserStatusDao advertiserStatusDao;

    @Override
    public List<AdvertiserStatus> getStatusListByAdvertiserId(long advertiserId) {
        return advertiserStatusDao.getStatusListByAdvertiserId(advertiserId);
    }

    @Override
    public void save(List<AdvertiserStatusDto> advertiserStatus, long advertiserId) {
        for (AdvertiserStatusDto status : advertiserStatus) {
            advertiserStatusDao.save(status, advertiserId);
        }
    }

    @Override
    public void update(List<AdvertiserStatusDto> advertiserStatus, long advertiserId) {
        for (AdvertiserStatusDto status : advertiserStatus) {
            if (isNull(status.getId())) {
                advertiserStatusDao.save(status, advertiserId);
            } else {
                advertiserStatusDao.update(status, advertiserId);
            }
        }
    }
}
