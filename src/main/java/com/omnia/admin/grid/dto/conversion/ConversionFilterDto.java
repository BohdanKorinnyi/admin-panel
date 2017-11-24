package com.omnia.admin.grid.dto.conversion;

import com.omnia.admin.grid.enums.conversion.ConversionFilterColumn;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.omnia.admin.grid.filter.FilterConstant.EMPTY;
import static com.omnia.admin.grid.filter.FilterConstant.SPACE;

@Setter
@AllArgsConstructor
public class ConversionFilterDto {
    private final String from;
    private final String to;
    private final String arbitratorName;
    private final String afids;
    private final String advertiserName;
    private final String status;
    private final String prefix;
    private final String timeZone;
    private final String offerName;

    public List<ConversionFilterColumn> getFields() {
        List<ConversionFilterColumn> fields = new ArrayList<>();
        if (!StringUtils.isEmpty(from) && !StringUtils.isEmpty(to)) {
            fields.add(ConversionFilterColumn.DATE);
        }
        if (!StringUtils.isEmpty(arbitratorName)) {
            fields.add(ConversionFilterColumn.ARBITRATOR_NAME);
        }
        if (!StringUtils.isEmpty(afids)) {
            fields.add(ConversionFilterColumn.AFFILIATES_ID);
        }
        if (!StringUtils.isEmpty(advertiserName)) {
            fields.add(ConversionFilterColumn.ADVERTISER_NAME);
        }
        if (!StringUtils.isEmpty(status)) {
            fields.add(ConversionFilterColumn.STATUS);
        }
        if (!StringUtils.isEmpty(prefix)) {
            fields.add(ConversionFilterColumn.PREFIX);
        }
        if (!StringUtils.isEmpty(timeZone)) {
            fields.add(ConversionFilterColumn.TIME_ZONE);
        }
        if (!StringUtils.isEmpty(offerName)) {
            fields.add(ConversionFilterColumn.OFFER_NAME);
        }
        return fields;
    }

    public String getFilterValue(ConversionFilterColumn conversionFilterColumn) {
        switch (conversionFilterColumn) {
            case DATE:
                return from + SPACE + to;
            case ARBITRATOR_NAME:
                return arbitratorName;
            case AFFILIATES_ID:
                return afids;
            case ADVERTISER_NAME:
                return advertiserName;
            case STATUS:
                return status;
            case PREFIX:
                return prefix;
            case TIME_ZONE:
                return timeZone;
            case OFFER_NAME:
                return offerName;
            default:
                return EMPTY;
        }
    }
}
