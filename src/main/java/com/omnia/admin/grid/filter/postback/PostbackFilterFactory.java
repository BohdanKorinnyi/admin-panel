package com.omnia.admin.grid.filter.postback;

import com.omnia.admin.grid.enums.postback.PostbackFilterColumn;
import com.omnia.admin.grid.filter.Filter;
import com.omnia.admin.grid.filter.impl.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.Map;

import static com.omnia.admin.grid.enums.postback.PostbackFilterColumn.*;
import static java.util.Objects.isNull;

@Component
public class PostbackFilterFactory {
    private static Map<PostbackFilterColumn, Filter> postbackFilters = new EnumMap<>(PostbackFilterColumn.class);

    @PostConstruct
    public void init() {
        postbackFilters.put(DEFAULT, new DefaultFilter());
        postbackFilters.put(DATE, new PostbackDateFilter());
        postbackFilters.put(BAYER, new BuyerFilter());
        postbackFilters.put(AFID, new PostbackAfIdFilter());
        postbackFilters.put(AFF_NET, new PostbackAfNetFilter());
        postbackFilters.put(STATUS, new PostbackStatusFilter());
        postbackFilters.put(PREFIX, new PostbackPrefixFilter());
        postbackFilters.put(TIME_ZONE, new PostbackTimeZoneFilter());
        postbackFilters.put(OFFER_NAME, new PostbackOfferNameFilter());
        postbackFilters.put(CLICK_ID, new PostbackClickIdFilter());
        postbackFilters.put(STATUS_FROM_OFFER_NAME, new DefaultFilter());
        postbackFilters.put(DUPLICATE, new PostbackDuplicateFilter());
    }

    public Filter getFilter(PostbackFilterColumn field) {
        Filter filter = postbackFilters.get(field);
        if (isNull(filter)) {
            return postbackFilters.get(PostbackFilterColumn.DEFAULT);
        }
        return filter;
    }
}
