package com.omnia.admin.dao;

import com.omnia.admin.dto.AdvertiserDto;
import com.omnia.admin.model.Advertiser;

import java.util.List;

public interface AdvertiserDao {
    List<Advertiser> findAll();

    List<String> getAllAdvertisersName();

    Number save(AdvertiserDto advertiser);

    void update(AdvertiserDto advertiser);
}
