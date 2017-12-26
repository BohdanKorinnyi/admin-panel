package com.omnia.admin.controller;

import com.omnia.admin.model.AdvertiserStatus;
import com.omnia.admin.service.AdvertiserStatusService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "advertiser/status")
public class AdvertiserStatusController {

    private final AdvertiserStatusService advertiserStatusService;

    @GetMapping(path = "get")
    public List<AdvertiserStatus> getStatusByAdvertiserId(@RequestParam("advertiserId") long advertiserId) {
        return advertiserStatusService.getStatusListByAdvertiserId(advertiserId);
    }
}
