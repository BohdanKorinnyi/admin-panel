package com.omnia.admin.service;

import com.omnia.admin.dto.AdvertiserDto;
import com.omnia.admin.model.Advertiser;

import java.util.List;

public interface AdvertiserService {
    List<Advertiser> getAllAdvertisers();

    List<String> getAdvertisersName();

    void update(List<AdvertiserDto> advertisers);

    Object report(List<Integer> advertiserIds, String from, String to);
}
