package com.omnia.admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdvertiserStatusDto {
    private Long id;
    private Long advId;
    private String name;
    private String type;
    private String realStatus;
}
