package com.omnia.admin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Expenses {
    private String buyer;
    private String date;
    private Float sum;
    @JsonProperty("type_name")
    private String name;
}
