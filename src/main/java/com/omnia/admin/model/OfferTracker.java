package com.omnia.admin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OfferTracker {
    private Long id;
    private String prefix;
    private Long offerTrackerId;
    private Long offerAdvId;
    private String name;
    private Integer type;
    private Integer upSell;
}
