package com.omnia.admin.grid.controller;

import com.omnia.admin.grid.dto.postback.PostbackGridFilterDetails;
import com.omnia.admin.grid.dto.postback.PostbackList;
import com.omnia.admin.grid.service.PostbackGridService;
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
@RequestMapping(value = "grid/postback")
@Api(value = "/grid/postback", description = "Operations about postback grid")
public class PostbackGridController {
    private final PostbackGridService postbackGridService;

    @PostMapping("get")
    @ApiOperation(value = "Returns postback list according to applied filter")
    public PostbackList getPostbackList(@ApiParam(value = "Filter object, size & page are required", required = true)
                                        @RequestBody PostbackGridFilterDetails filterDetails) {
        return postbackGridService.getPostbackList(filterDetails);
    }
}
