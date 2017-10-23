package com.omnia.admin.grid.dto.postback;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class PostbackList {
    private final int size;
    private final List<Map<String, Object>> postbacks;
}
