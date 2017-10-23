package com.omnia.admin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdvertiserOffer {
    private Long id;
    private Long advertiserId;
    private String offerIdAdverts;
    private String offerRisk;
    private String name;
}
