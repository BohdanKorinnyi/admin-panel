package com.omnia.admin.grid.dto.conversion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ConversionList {
    private final int size;
    private final List<ConversionDto> conversions;
}
