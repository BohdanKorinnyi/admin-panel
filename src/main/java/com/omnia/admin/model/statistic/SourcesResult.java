package com.omnia.admin.model.statistic;

import com.omnia.admin.model.Source;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SourcesResult extends BuyerDetails {
    private List<Source> data;
}
