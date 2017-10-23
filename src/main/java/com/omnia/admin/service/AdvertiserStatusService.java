package com.omnia.admin.service;

import com.omnia.admin.dto.AdvertiserStatusDto;
import com.omnia.admin.model.AdvertiserStatus;

import java.util.List;

public interface AdvertiserStatusService {
    List<AdvertiserStatus> getStatusListByAdvertiserId(long advertiserId);

    void save(List<AdvertiserStatusDto> advertiserStatus, long advertiserId);

    void update(List<AdvertiserStatusDto> advertiserStatus, long advertiserId);
}
