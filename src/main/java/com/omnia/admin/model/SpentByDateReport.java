package com.omnia.admin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SpentByDateReport {
    private String date;
    private List<SpentBySourceReport> sources;
}
