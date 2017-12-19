package com.omnia.admin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SpentBySourceReport {
    private String source;
    private List<BuyerCosts> costs;
}
