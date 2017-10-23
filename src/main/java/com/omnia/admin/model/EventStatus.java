package com.omnia.admin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventStatus {
    private Long id;
    private Long advertiserId;
    private String advertiserName;
    private String status;
    private String eventName;
}
