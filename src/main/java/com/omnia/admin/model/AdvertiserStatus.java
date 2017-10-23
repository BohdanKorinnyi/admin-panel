package com.omnia.admin.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdvertiserStatus {
    private Long id;
    private Long advId;
    private String name;
    private String type;
    private String realStatus;
}
