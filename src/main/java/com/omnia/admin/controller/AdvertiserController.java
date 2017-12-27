package com.omnia.admin.controller;

import com.omnia.admin.dto.AdvertiserDto;
import com.omnia.admin.model.Advertiser;
import com.omnia.admin.model.Role;
import com.omnia.admin.security.annotation.RequiredRole;
import com.omnia.admin.service.AdvertiserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    @RequiredRole(roles = {Role.ADMIN, Role.DIRECTOR, Role.CFO, Role.CBO})
    public void update(@RequestBody List<AdvertiserDto> advertisers) {
        advertiserService.update(advertisers);
    }

    @GetMapping("report")
    public ResponseEntity getReport(@RequestParam(required = false) List<Integer> advertiserIds,
                                    @RequestParam String from,
                                    @RequestParam String to) {
        return ResponseEntity.ok(advertiserService.report(advertiserIds, from, to));
    }
}
