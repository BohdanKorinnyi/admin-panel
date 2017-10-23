package com.omnia.admin.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@Builder
public class Conversion {
    private Long id;
    private Integer statusId;
    private Double sum;
    private Integer afId;
    private String prefix;
    private String clickId;
    private Long offerTrackerId;
    private Date creation;
    private Date change;
    private Integer currencyId;
    private Integer exchangeId;
    private Integer sourceTrackerId;
    private String campaignId;
}
