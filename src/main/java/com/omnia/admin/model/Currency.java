package com.omnia.admin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Currency {
    private Long id;
    private String code;
    private String description;
    private Long sync;
    private Long count;
}
