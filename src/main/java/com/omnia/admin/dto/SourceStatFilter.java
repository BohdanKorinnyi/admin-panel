package com.omnia.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SourceStatFilter {
    private  int page;
    private  int size;
    private  List<String> buyers;
    private  List<String> types;
    private  String from;
    private  String to;
}
