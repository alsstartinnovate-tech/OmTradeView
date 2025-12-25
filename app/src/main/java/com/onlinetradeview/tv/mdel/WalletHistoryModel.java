package com.onlinetradeview.tv.mdel;

public class WalletHistoryModel {
    private String username, str_amount, str_datetime, str_description, str_type, str_bp, str_ap;

    public WalletHistoryModel(String username, String str_amount, String str_datetime, String str_description,
                              String str_type, String str_bp, String str_ap) {
        this.username = username;
        this.str_amount = str_amount;
        this.str_datetime = str_datetime;
        this.str_description = str_description;
        this.str_type = str_type;
        this.str_bp = str_bp;
        this.str_ap = str_ap;
    }

    public String getStr_bp() {
        return str_bp;
    }

    public void setStr_bp(String str_bp) {
        this.str_bp = str_bp;
    }

    public String getStr_ap() {
        return str_ap;
    }

    public void setStr_ap(String str_ap) {
        this.str_ap = str_ap;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStr_amount() {
        return str_amount;
    }

    public void setStr_amount(String str_amount) {
        this.str_amount = str_amount;
    }

    public String getStr_datetime() {
        return str_datetime;
    }

    public void setStr_datetime(String str_datetime) {
        this.str_datetime = str_datetime;
    }

    public String getStr_description() {
        return str_description;
    }

    public void setStr_description(String str_description) {
        this.str_description = str_description;
    }

    public String getStr_type() {
        return str_type;
    }

    public void setStr_type(String str_type) {
        this.str_type = str_type;
    }
}
