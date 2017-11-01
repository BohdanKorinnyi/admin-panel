package com.omnia.admin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Buyer {
    private Long id;
    private String name;
    private String type;
    private String comment;
    private int planRev;
    private int planProfit;
    private int planRevOld;
    private int planProfitOld;
}
