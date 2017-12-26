package com.omnia.admin.grid.controller;

import com.omnia.admin.grid.dto.conversion.ConversionGridFilterDetails;
import com.omnia.admin.grid.dto.conversion.ConversionList;
import com.omnia.admin.grid.service.ConversionGridService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "grid/conversions")
public class ConversionGridController {

    private final ConversionGridService conversionGridService;

    @PostMapping("get")
    public ConversionList getConversions(@RequestBody ConversionGridFilterDetails filterDetails) {
        return conversionGridService.getConversions(filterDetails);
    }
}
