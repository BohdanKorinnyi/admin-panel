package com.omnia.admin.grid.controller;

import com.omnia.admin.grid.dto.postback.PostbackFilterDto;
import com.omnia.admin.grid.dto.postback.PostbackGridFilterDetails;
import com.omnia.admin.grid.dto.postback.PostbackList;
import com.omnia.admin.grid.service.PostbackGridService;
import com.omnia.admin.model.Role;
import com.omnia.admin.service.BuyerService;
import com.omnia.admin.util.UserPrincipalUtils;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static java.util.Objects.isNull;

@RestController
@AllArgsConstructor
@RequestMapping(value = "grid/postback")
public class PostbackGridController {
    private final PostbackGridService postbackGridService;
    private final BuyerService buyerService;

    @PostMapping("get")
    public PostbackList getPostbackList(HttpServletRequest request, @RequestBody PostbackGridFilterDetails filterDetails) {
        if (UserPrincipalUtils.isRole(request, Role.BUYER)) {
            if (isNull(filterDetails.getFilter())) {
                filterDetails.setFilter(new PostbackFilterDto());
            }
            PostbackFilterDto filter = filterDetails.getFilter();
            filter.setBuyer(buyerService.getBuyerById(UserPrincipalUtils.getBuyerId(request)));
            return postbackGridService.getPostbackList(filterDetails);
        }
        return postbackGridService.getPostbackList(filterDetails);
    }
}
