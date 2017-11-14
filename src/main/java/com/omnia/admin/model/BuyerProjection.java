package com.omnia.admin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class BuyerProjection {
    private final int buyerId;
    private final String buyerName;
}
