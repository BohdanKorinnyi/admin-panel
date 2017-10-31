package com.omnia.admin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Currency {
    private Long id;
    private String code;
    private String descriptions;
    private Long sync;
    private Long count;
}
