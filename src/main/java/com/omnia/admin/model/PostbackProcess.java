package com.omnia.admin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostbackProcess {
    private Long id;
    private Long postbackId;
    private Integer status;
}
