package com.omnia.admin.controller;

import com.omnia.admin.dto.AdvertiserDto;
import com.omnia.admin.model.Advertiser;
import com.omnia.admin.service.AdvertiserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "Operations about advertiser")
@RestController
@AllArgsConstructor
@RequestMapping(value = "advertiser")
public class AdvertiserController {

    private final AdvertiserService advertiserService;

    @GetMapping(path = "all")
    @ApiOperation(value = "Returns list of all advertisers")
    public List<Advertiser> getAllAdvertisers() {
        return advertiserService.getAllAdvertisers();
    }

    @GetMapping(path = "/names")
    @ApiOperation(value = "Returns names of all advertisers, can be use on the grid for filter by advertiser name")
    public List<String> getAllAdvertisersName() {
        return advertiserService.getAdvertisersName();
    }

    @PutMapping(path = "update")
    @ApiOperation(value = "Update advertisers")
    public void update(@ApiParam(value = "Updated advertisers, statuses with empty 'id' will be created, with id will be updated")
                       @RequestBody List<AdvertiserDto> advertisers) {
        advertiserService.update(advertisers);
    }
}
