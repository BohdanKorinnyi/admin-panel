package com.omnia.admin.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Advertiser {
    private Long id;
    private String advname;
    private String advshortname;
    private String secretKey;
    private String url;
    private String apiKey;
    private Long risk;
    private Integer timezone;
}
