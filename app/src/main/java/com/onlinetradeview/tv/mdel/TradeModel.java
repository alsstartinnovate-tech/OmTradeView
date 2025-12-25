package com.onlinetradeview.tv.mdel;

import android.graphics.drawable.Drawable;

public class TradeModel {
    private String id, name, token, soldtrader, soldnumber, datesold, datesoldmarket, pricebought, qtybought, boughttrader,
            boughtradernumber, datebought, dateboughtmarket, str_order_type, str_type, str_status, str_date_expiry;
    private String usedMargin="", holdingMarginReq="", str_avg_bidprice, strPlPRice, strCmp, avgBuyPrice, avgSellPrice, brokerage;
    private String ordertype_bought, soldPrice, profitloss, str_catId, str_plbalanceClose, str_order_status;

    private String orderTypeSoldText;
    private int orderTypeSoldTc;
    private Drawable orderTypeSoldBg;

    private String orderTypeBoughtText;
    private int orderTypeBoughtTc;
    private Drawable orderTypeBoughtBg;

    private int tcProfitLossMrktQty;
    private Drawable bgProfitLossMrktQty;

    private int soldTypeLotTc, priceBoughtTc;
    private Drawable soldTypeLotBg, priceBoughtBg;

    public int getTcProfitLossMrktQty() {
        return tcProfitLossMrktQty;
    }

    public void setTcProfitLossMrktQty(int tcProfitLossMrktQty) {
        this.tcProfitLossMrktQty = tcProfitLossMrktQty;
    }

    public Drawable getBgProfitLossMrktQty() {
        return bgProfitLossMrktQty;
    }

    public void setBgProfitLossMrktQty(Drawable bgProfitLossMrktQty) {
        this.bgProfitLossMrktQty = bgProfitLossMrktQty;
    }

    public int getSoldTypeLotTc() {
        return soldTypeLotTc;
    }

    public void setSoldTypeLotTc(int soldTypeLotTc) {
        this.soldTypeLotTc = soldTypeLotTc;
    }

    public int getPriceBoughtTc() {
        return priceBoughtTc;
    }

    public void setPriceBoughtTc(int priceBoughtTc) {
        this.priceBoughtTc = priceBoughtTc;
    }

    public Drawable getSoldTypeLotBg() {
        return soldTypeLotBg;
    }

    public void setSoldTypeLotBg(Drawable soldTypeLotBg) {
        this.soldTypeLotBg = soldTypeLotBg;
    }

    public Drawable getPriceBoughtBg() {
        return priceBoughtBg;
    }

    public void setPriceBoughtBg(Drawable priceBoughtBg) {
        this.priceBoughtBg = priceBoughtBg;
    }

    public String getOrderTypeBoughtText() {
        return orderTypeBoughtText;
    }

    public void setOrderTypeBoughtText(String orderTypeBoughtText) {
        this.orderTypeBoughtText = orderTypeBoughtText;
    }

    public int getOrderTypeBoughtTc() {
        return orderTypeBoughtTc;
    }

    public void setOrderTypeBoughtTc(int orderTypeBoughtTc) {
        this.orderTypeBoughtTc = orderTypeBoughtTc;
    }

    public Drawable getOrderTypeBoughtBg() {
        return orderTypeBoughtBg;
    }

    public void setOrderTypeBoughtBg(Drawable orderTypeBoughtBg) {
        this.orderTypeBoughtBg = orderTypeBoughtBg;
    }

    public String getOrderTypeSoldText() {
        return orderTypeSoldText;
    }

    public void setOrderTypeSoldText(String orderTypeSoldText) {
        this.orderTypeSoldText = orderTypeSoldText;
    }

    public Drawable getOrderTypeSoldBg() {
        return orderTypeSoldBg;
    }

    public void setOrderTypeSoldBg(Drawable orderTypeSoldBg) {
        this.orderTypeSoldBg = orderTypeSoldBg;
    }

    public int getOrderTypeSoldTc() {
        return orderTypeSoldTc;
    }

    public void setOrderTypeSoldTc(int orderTypeSoldTc) {
        this.orderTypeSoldTc = orderTypeSoldTc;
    }

    public TradeModel() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStr_order_status() {
        return str_order_status;
    }

    public void setStr_order_status(String str_order_status) {
        this.str_order_status = str_order_status;
    }

    public String getStr_plbalanceClose() {
        return str_plbalanceClose;
    }

    public void setStr_plbalanceClose(String str_plbalanceClose) {
        this.str_plbalanceClose = str_plbalanceClose;
    }

    public String getAvgBuyPrice() {
        return avgBuyPrice;
    }

    public void setAvgBuyPrice(String avgBuyPrice) {
        this.avgBuyPrice = avgBuyPrice;
    }

    public String getAvgSellPrice() {
        return avgSellPrice;
    }

    public void setAvgSellPrice(String avgSellPrice) {
        this.avgSellPrice = avgSellPrice;
    }

    public String getStr_catId() {
        return str_catId;
    }

    public void setStr_catId(String str_catId) {
        this.str_catId = str_catId;
    }

    public String getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(String brokerage) {
        this.brokerage = brokerage;
    }

    public String getOrdertype_bought() {
        return ordertype_bought;
    }

    public void setOrdertype_bought(String ordertype_bought) {
        this.ordertype_bought = ordertype_bought;
    }

    public String getSoldPrice() {
        return soldPrice;
    }

    public void setSoldPrice(String soldPrice) {
        this.soldPrice = soldPrice;
    }

    public String getProfitloss() {
        return profitloss;
    }

    public void setProfitloss(String profitloss) {
        this.profitloss = profitloss;
    }

    public String getStrPlPRice() {
        return strPlPRice;
    }

    public void setStrPlPRice(String strPlPRice) {
        this.strPlPRice = strPlPRice;
    }

    public String getStrCmp() {
        return strCmp;
    }

    public void setStrCmp(String strCmp) {
        this.strCmp = strCmp;
    }

    public String getStr_status() {
        return str_status;
    }

    public void setStr_status(String str_status) {
        this.str_status = str_status;
    }

    public String getStr_avg_bidprice() {
        return str_avg_bidprice;
    }

    public void setStr_avg_bidprice(String str_avg_bidprice) {
        this.str_avg_bidprice = str_avg_bidprice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsedMargin() {
        return usedMargin;
    }

    public void setUsedMargin(String usedMargin) {
        this.usedMargin = usedMargin;
    }

    public String getHoldingMarginReq() {
        return holdingMarginReq;
    }

    public void setHoldingMarginReq(String holdingMarginReq) {
        this.holdingMarginReq = holdingMarginReq;
    }

    public String getStr_order_type() {
        return str_order_type;
    }

    public void setStr_order_type(String str_order_type) {
        this.str_order_type = str_order_type;
    }

    public String getStr_type() {
        return str_type;
    }

    public void setStr_type(String str_type) {
        this.str_type = str_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSoldtrader() {
        return soldtrader;
    }

    public void setSoldtrader(String soldtrader) {
        this.soldtrader = soldtrader;
    }

    public String getSoldnumber() {
        return soldnumber;
    }

    public void setSoldnumber(String soldnumber) {
        this.soldnumber = soldnumber;
    }

    public String getDatesold() {
        return datesold;
    }

    public void setDatesold(String datesold) {
        this.datesold = datesold;
    }

    public String getDatesoldmarket() {
        return datesoldmarket;
    }

    public void setDatesoldmarket(String datesoldmarket) {
        this.datesoldmarket = datesoldmarket;
    }

    public String getPricebought() {
        return pricebought;
    }

    public void setPricebought(String pricebought) {
        this.pricebought = pricebought;
    }

    public String getQtybought() {
        return qtybought;
    }

    public void setQtybought(String qtybought) {
        this.qtybought = qtybought;
    }

    public String getBoughttrader() {
        return boughttrader;
    }

    public void setBoughttrader(String boughttrader) {
        this.boughttrader = boughttrader;
    }

    public String getBoughtradernumber() {
        return boughtradernumber;
    }

    public void setBoughtradernumber(String boughtradernumber) {
        this.boughtradernumber = boughtradernumber;
    }

    public String getDatebought() {
        return datebought;
    }

    public void setDatebought(String datebought) {
        this.datebought = datebought;
    }

    public String getDateboughtmarket() {
        return dateboughtmarket;
    }

    public void setDateboughtmarket(String dateboughtmarket) {
        this.dateboughtmarket = dateboughtmarket;
    }

    public String getStr_date_expiry() {
        return str_date_expiry;
    }

    public void setStr_date_expiry(String str_date_expiry) {
        this.str_date_expiry = str_date_expiry;
    }

}