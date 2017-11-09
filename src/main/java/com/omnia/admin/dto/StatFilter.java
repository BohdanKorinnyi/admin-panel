package com.omnia.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StatFilter {
    private final List<String> buyers;
    private final List<String> types;
    private final String from;
    private final String to;
}
