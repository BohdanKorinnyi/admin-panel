package com.omnia.admin.grid.controller;

import com.omnia.admin.grid.dto.conversion.ConversionGridFilterDetails;
import com.omnia.admin.grid.dto.conversion.ConversionList;
import com.omnia.admin.grid.service.ConversionGridService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "grid/conversions")
@Api(value = "/grid/conversions", description = "Operations about conversion grid")
public class ConversionGridController {

    private final ConversionGridService conversionGridService;

    @PostMapping("get")
    @ApiOperation(value = "Returns conversions list according to applied filter")
    public ConversionList getConversions(@ApiParam(value = "Filter object, size & page are required", required = true)
                                         @RequestBody ConversionGridFilterDetails filterDetails) {
        return conversionGridService.getConversions(filterDetails);
    }
}
