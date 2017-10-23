package com.omnia.admin.controller;

import com.omnia.admin.dto.AdvertiserDto;
import com.omnia.admin.model.Advertiser;
import com.omnia.admin.service.AdvertiserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "advertiser")
public class AdvertiserController {

    private final AdvertiserService advertiserService;

    @GetMapping(path = "all")
    public List<Advertiser> getAllAdvertisers() {
        return advertiserService.getAllAdvertisers();
    }

    @GetMapping(path = "/names")
    public List<String> getAllAdvertisersName() {
        return advertiserService.getAdvertisersName();
    }

    @PutMapping(path = "update")
    public void update(@RequestBody List<AdvertiserDto> advertisers) {
        advertiserService.update(advertisers);
    }
}
