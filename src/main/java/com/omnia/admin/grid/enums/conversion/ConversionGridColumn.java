package com.omnia.admin.grid.enums.conversion;

import java.util.StringJoiner;

import static com.omnia.admin.grid.filter.FilterConstant.COMMA;

public enum ConversionGridColumn {
    CONVERSION_ID("conversions.id"),
    CONVERSION_PREFIX("conversions.prefix"),
    CONVERSION_AFID("conversions.afid"),
    AFFILIATES_AFNAME("affiliates.afname"),
    CONVERSION_DATA_CREATION("conversions.data_creation"),
    CONVERSION_SUM("conversions.sum"),
    CURRENCY_CODE("currency.code"),
    ADV_STATUS_REAL_STATUS("adv_status.real_status"),
    ADVERTS_SHORT_NAME("adverts.advshortname"),
    EXCHANGE_RATE("exchange.rate"),
    CONVERSION_DATE_CHANGE("conversions.date_change"),
    CONVERSION_CLICK_ID("conversions.clickid");

    private String value;

    ConversionGridColumn(String column) {
        this.value = column;
    }

    public String getValue() {
        return value;
    }

    public static String getColumnList() {
        StringJoiner columns = new StringJoiner(COMMA);
        for (ConversionGridColumn column : ConversionGridColumn.values()) {
            columns.add(column.getValue());
        }
        return columns.toString();
    }
}
