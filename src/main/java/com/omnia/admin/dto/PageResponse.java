package com.omnia.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageResponse {
    private int total;
    private int page;
    private Object data;
}
