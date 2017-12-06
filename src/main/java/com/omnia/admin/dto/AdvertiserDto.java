package com.omnia.admin.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AdvertiserDto {
    private Long id;
    private String advname;
    private String advshortname;
    private String secretKey;
    private String url;
    private String apiKey;
    private Integer risk;
    private List<AdvertiserStatusDto> statuses;
    private Integer timezone;
}
