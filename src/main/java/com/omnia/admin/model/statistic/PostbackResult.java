package com.omnia.admin.model.statistic;

import com.omnia.admin.service.PostbackStats;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostbackResult extends BuyerDetails {
    private List<PostbackStats> data;
}
