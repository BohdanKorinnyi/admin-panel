package com.omnia.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StatFilter {
    private  int page;
    private  int size;
    private  List<String> buyers;
    private  List<String> types;
    private  String from;
    private  String to;
}
