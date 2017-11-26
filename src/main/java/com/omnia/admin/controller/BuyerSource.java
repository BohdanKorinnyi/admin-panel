package com.omnia.admin.controller;

import com.omnia.admin.model.SourceStat;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BuyerSource {
    private int buyerId;
    private String buyer;
    private List<SourceStat> data;
}
