package com.omnia.admin.dao;

import com.omnia.admin.dto.AdvertiserStatusDto;
import com.omnia.admin.model.AdvertiserStatus;

import java.util.List;

public interface AdvertiserStatusDao {
    List<AdvertiserStatus> getStatusListByAdvertiserId(long advertiserId);

    void update(AdvertiserStatusDto status, long advertiserId);

    void save(AdvertiserStatusDto status, long advertiserId);

    void deleteAdvertiserStatuses(long advertiserId);
}
