package com.omnia.admin.model;

import lombok.*;

import java.sql.Date;
import java.sql.Time;

@Getter
@Setter
public class Postback {
    private Long id;
    private Integer afid;
    private String prefix;
    private Date date;
    private Time time;
    private String clickId;
    private String offerId;
    private Double sum;
    private String currency;
    private String goal;
    private String status;
    private String advname;
    private String offerName;
    private String transactionId;
    private String idfa;
    private String gaid;
    private String t1;
    private String t2;
    private String t3;
    private String t4;
    private String t5;
    private String t6;
    private String t7;
    private String t8;
    private String t9;
    private String t10;
    private String secretKey;
    private String ipAddress;
    private String fullUrl;
    private Long postBacksend;
    private String duplicate;
    private String event1;
    private String event2;
    private String event3;
    private String event4;
    private String event5;
    private String event6;
    private String event7;
    private String event8;
    private String event9;
    private String event10;
    private Integer exchange;
}
