package com.omnia.admin.grid.dto.postback;

import lombok.Getter;

import java.util.StringJoiner;

import static com.omnia.admin.grid.filter.FilterConstant.COMMA;

@Getter
public enum PostbackGridColumn {
    POSTBACK_ID("postback.id"),
    PREFIX("postback.prefix"),
    AF_ID("postback.afid"),
    BUYER("affiliates.afname"),
    DATA("postback.date"),
    TIME("postback.time"),
    PAYOUT("postback.sum"),
    CURRENCY("currency.code"),
    STATUS("postback.status"),
    AFF_NET("postback.advname"),
    OFFER_ID("postback.offerid"),
    OFFER_NAME("postback.offername"),
    COM_ID("postback.idc"),
    OID("postback.ido"),

    TOKEN_T1("postback.t1"),
    TOKEN_T2("postback.t2"),
    TOKEN_T3("postback.t3"),
    TOKEN_T4("postback.t4"),
    TOKEN_T5("postback.t5"),
    TOKEN_T6("postback.t6"),
    TOKEN_T7("postback.t7"),
    TOKEN_T8("postback.t8"),
    TOKEN_T9("postback.t9"),
    TOKEN_T10("postback.t10"),

    CLICK_ID("postback.clickid"),
    TRANSACTION_ID("postback.transaction_id"),
    DUPLICATE("postback.duplicate");

    private String name;

    PostbackGridColumn(String name) {
        this.name = name;
    }

    public static String getColumnList() {
        StringJoiner columns = new StringJoiner(COMMA);
        for (PostbackGridColumn column : PostbackGridColumn.values()) {
            columns.add(column.getName());
        }
        return columns.toString();
    }
}
