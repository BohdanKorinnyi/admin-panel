package com.omnia.admin.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public final class Expenses {
    private Long id;
    private Integer buyerId;
    private String buyer;
    private String date;
    private Float sum;
    private String name;

    private Integer typeId;
    private String description;
    private Date dateCreate;
    private Date dateChange;
}
