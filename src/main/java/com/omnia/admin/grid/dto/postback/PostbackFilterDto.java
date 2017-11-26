package com.omnia.admin.grid.dto.postback;

import com.omnia.admin.grid.enums.postback.PostbackFilterColumn;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.omnia.admin.grid.filter.FilterConstant.EMPTY;
import static com.omnia.admin.grid.filter.FilterConstant.SPACE;

@Getter
@Setter
public class PostbackFilterDto {
    private String from;
    private String to;
    private String buyer;
    private String afid;
    private String advertiser;
    private String status;
    private String prefix;
    private String timeZone;
    private String offerName;
    private String clickId;
    private String statusFromOfferName;
    private String duplicate;

    public List<PostbackFilterColumn> getFields() {
        List<PostbackFilterColumn> fields = new ArrayList<>();
        if (!StringUtils.isEmpty(from) && !StringUtils.isEmpty(to)) {
            fields.add(PostbackFilterColumn.DATE);
        }
        if (!StringUtils.isEmpty(buyer)) {
            fields.add(PostbackFilterColumn.BAYER);
        }
        if (!StringUtils.isEmpty(afid)) {
            fields.add(PostbackFilterColumn.AFID);
        }
        if (!StringUtils.isEmpty(advertiser)) {
            fields.add(PostbackFilterColumn.AFF_NET);
        }
        if (!StringUtils.isEmpty(status)) {
            fields.add(PostbackFilterColumn.STATUS);
        }
        if (!StringUtils.isEmpty(prefix)) {
            fields.add(PostbackFilterColumn.PREFIX);
        }
        if (!StringUtils.isEmpty(timeZone)) {
            fields.add(PostbackFilterColumn.TIME_ZONE);
        }
        if (!StringUtils.isEmpty(offerName)) {
            fields.add(PostbackFilterColumn.OFFER_NAME);
        }
        if (!StringUtils.isEmpty(clickId)) {
            fields.add(PostbackFilterColumn.CLICK_ID);
        }
        if (!StringUtils.isEmpty(statusFromOfferName)) {
            fields.add(PostbackFilterColumn.STATUS_FROM_OFFER_NAME);
        }
        if (!StringUtils.isEmpty(duplicate)) {
            fields.add(PostbackFilterColumn.DUPLICATE);
        }
        return fields;
    }

    public String getFilterValue(PostbackFilterColumn postbackFilterColumn) {
        switch (postbackFilterColumn) {
            case DATE:
                return from + SPACE + to;
            case BAYER:
                return buyer;
            case AFID:
                return afid;
            case AFF_NET:
                return advertiser;
            case STATUS:
                return status;
            case PREFIX:
                return prefix;
            case TIME_ZONE:
                return timeZone;
            case OFFER_NAME:
                return offerName;
            case CLICK_ID:
                return clickId;
            case STATUS_FROM_OFFER_NAME:
                return statusFromOfferName;
            case DUPLICATE:
                return duplicate;
            default:
                return EMPTY;
        }
    }
}
