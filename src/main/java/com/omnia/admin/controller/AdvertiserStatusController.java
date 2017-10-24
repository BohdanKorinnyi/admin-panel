package com.omnia.admin.controller;

import com.omnia.admin.model.AdvertiserStatus;
import com.omnia.admin.service.AdvertiserStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "Operations about advertiser statuses")
@RestController
@AllArgsConstructor
@RequestMapping(value = "advertiser/status")
public class AdvertiserStatusController {

    private final AdvertiserStatusService advertiserStatusService;

    @GetMapping(path = "get")
    @ApiOperation(value = "Returns statuses of requested advertiser")
    @ApiResponse(code = 200, message = "Array of advertiser's statuses")
    public List<AdvertiserStatus> getStatusByAdvertiserId(@ApiParam(value = "Advertiser Id", required = true) @RequestParam("advertiserId") long advertiserId) {
        return advertiserStatusService.getStatusListByAdvertiserId(advertiserId);
    }
}
