package com.omnia.admin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Staff {
    private Long id;
    private Long buyerId;
    private Long type;
    private String firstName;
    private String secodName;
    private String firstDay;
    private String lastDay;
}
