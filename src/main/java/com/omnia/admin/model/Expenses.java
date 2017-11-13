package com.omnia.admin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public final class Expenses {
    private final String buyer;
    private final String date;
    private final Float sum;
    @JsonProperty("type_name")
    private final String name;
}
