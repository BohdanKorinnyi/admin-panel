package com.omnia.admin.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class PostbackStats {
    private Integer buyerId;
    private String date;
    private String name;
    private String advname;
    private String status;
    private Float amountInUsd;
}
