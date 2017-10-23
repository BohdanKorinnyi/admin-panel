package com.omnia.admin.grid.dto.conversion;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ConversionDto {
    private int conversionSize;
    private Integer conversionId;
    private String prefix;
    private Integer afId;
    private String buyer;
    private Date date;
    private Float payout;
    private String status;
    private String advert;
    private String offerId;
    private String trafficSource;
    private Integer rate;
    private Date change;
    private String clickId;
    private String code;
}
