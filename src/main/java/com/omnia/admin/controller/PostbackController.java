package com.omnia.admin.controller;

import com.google.common.collect.ImmutableMap;
import com.omnia.admin.service.PostbackService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("postback")
public class PostbackController {

    private final PostbackService postbackService;

    @GetMapping("fullurl")
    public ResponseEntity getFullUrl(@RequestParam("id") Long postbackId) {
        Optional<String> fullUrl = postbackService.getFullUrl(postbackId);
        if (fullUrl.isPresent()) {
            return ResponseEntity.ok(ImmutableMap.of("fullurl", fullUrl.get()));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
