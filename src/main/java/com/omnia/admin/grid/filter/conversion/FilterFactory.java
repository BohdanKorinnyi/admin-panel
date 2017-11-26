package com.omnia.admin.grid.filter.conversion;

import com.omnia.admin.grid.enums.conversion.ConversionFilterColumn;
import com.omnia.admin.grid.filter.Filter;
import com.omnia.admin.grid.filter.impl.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.Map;

import static com.omnia.admin.grid.enums.conversion.ConversionFilterColumn.*;
import static java.util.Objects.isNull;

@Component
public final class FilterFactory {

    private static Map<ConversionFilterColumn, Filter> conversionFilters = new EnumMap<>(ConversionFilterColumn.class);

    @PostConstruct
    public void init() {
        conversionFilters.put(DEFAULT, new DefaultFilter());
        conversionFilters.put(DATE, new DateFilter());
        conversionFilters.put(ARBITRATOR_NAME, new ArbitratorNameFilter());
        conversionFilters.put(AFFILIATES_ID, new AffiliatesIdFilter());
        conversionFilters.put(ADVERTISER_NAME, new AdvertiserNameFilter());
        conversionFilters.put(STATUS, new StatusFilter());
        conversionFilters.put(PREFIX, new PrefixFilter());
        conversionFilters.put(TIME_ZONE, new TimeZoneFilter());
        conversionFilters.put(OFFER_NAME, new BuyerFilter());
    }

    public Filter getFilter(ConversionFilterColumn field) {
        Filter filter = conversionFilters.get(field);
        if (isNull(filter)) {
            return conversionFilters.get(ConversionFilterColumn.DEFAULT);
        }
        return filter;
    }
}
