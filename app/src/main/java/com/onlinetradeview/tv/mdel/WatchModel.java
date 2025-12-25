package com.onlinetradeview.tv.mdel;

public class WatchModel {
    private String Exchange, InstrumentIdentifier, LastTradeTime, ServerTime, AverageTradedPrice, BuyPrice, BuyQty,
            Close, High, Low, LastTradePrice, LastTradeQty, Open, OpenInterest, QuotationLot, SellPrice, SellQty, TotalQtyTraded,
            Value, PreOpen, PriceChange, PriceChangePercentage, OpenInterestChange, MessageType, expiryDate, instrumentName, getCatId, lowerCkt, upperCkt;
    private String jsonText;
    private boolean isSelected = true;

    public WatchModel(String exchange, String instrumentIdentifier, String lastTradeTime, String serverTime, String averageTradedPrice, String buyPrice, String buyQty,
                      String close, String high, String low, String lastTradePrice, String lastTradeQty, String open, String openInterest, String quotationLot,
                      String sellPrice, String sellQty, String totalQtyTraded, String value, String preOpen, String priceChange, String priceChangePercentage,
                      String openInterestChange, String messageType, String expiryDate, String instrumentName, boolean isSelected,
                      String getCatId, String lowerCkt, String upperCkt) {
        this.lowerCkt = lowerCkt;
        this.upperCkt = upperCkt;
        this.expiryDate = expiryDate;
        this.instrumentName = instrumentName;
        this.isSelected = isSelected;
        Exchange = exchange;
        InstrumentIdentifier = instrumentIdentifier;
        LastTradeTime = lastTradeTime;
        ServerTime = serverTime;
        AverageTradedPrice = averageTradedPrice;
        BuyPrice = buyPrice;
        BuyQty = buyQty;
        Close = close;
        High = high;
        Low = low;
        LastTradePrice = lastTradePrice;
        LastTradeQty = lastTradeQty;
        Open = open;
        OpenInterest = openInterest;
        QuotationLot = quotationLot;
        SellPrice = sellPrice;
        SellQty = sellQty;
        TotalQtyTraded = totalQtyTraded;
        Value = value;
        PreOpen = preOpen;
        PriceChange = priceChange;
        PriceChangePercentage = priceChangePercentage;
        OpenInterestChange = openInterestChange;
        MessageType = messageType;
        this.isSelected = isSelected;
        this.getCatId = getCatId;
    }

    public String getLowerCkt() {
        return lowerCkt;
    }

    public void setLowerCkt(String lowerCkt) {
        this.lowerCkt = lowerCkt;
    }

    public String getUpperCkt() {
        return upperCkt;
    }

    public void setUpperCkt(String upperCkt) {
        this.upperCkt = upperCkt;
    }

    public WatchModel(String exchange, String instrumentIdentifier, String jsonText) {
        Exchange = exchange;
        InstrumentIdentifier = instrumentIdentifier;
        this.jsonText = jsonText;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getExchange() {
        return Exchange;
    }

    public void setExchange(String exchange) {
        Exchange = exchange;
    }

    public String getInstrumentIdentifier() {
        return InstrumentIdentifier;
    }

    public String getGetCatId() {
        return getCatId;
    }

    public void setGetCatId(String getCatId) {
        this.getCatId = getCatId;
    }

    public void setInstrumentIdentifier(String instrumentIdentifier) {
        InstrumentIdentifier = instrumentIdentifier;
    }

    public String getLastTradeTime() {
        return LastTradeTime;
    }

    public void setLastTradeTime(String lastTradeTime) {
        LastTradeTime = lastTradeTime;
    }

    public String getServerTime() {
        return ServerTime;
    }

    public void setServerTime(String serverTime) {
        ServerTime = serverTime;
    }

    public String getAverageTradedPrice() {
        return AverageTradedPrice;
    }

    public void setAverageTradedPrice(String averageTradedPrice) {
        AverageTradedPrice = averageTradedPrice;
    }

    public String getBuyPrice() {
        return BuyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        BuyPrice = buyPrice;
    }

    public String getBuyQty() {
        return BuyQty;
    }

    public void setBuyQty(String buyQty) {
        BuyQty = buyQty;
    }

    public String getClose() {
        return Close;
    }

    public void setClose(String close) {
        Close = close;
    }

    public String getHigh() {
        return High;
    }

    public void setHigh(String high) {
        High = high;
    }

    public String getLow() {
        return Low;
    }

    public void setLow(String low) {
        Low = low;
    }

    public String getLastTradePrice() {
        return LastTradePrice;
    }

    public void setLastTradePrice(String lastTradePrice) {
        LastTradePrice = lastTradePrice;
    }

    public String getLastTradeQty() {
        return LastTradeQty;
    }

    public void setLastTradeQty(String lastTradeQty) {
        LastTradeQty = lastTradeQty;
    }

    public String getOpen() {
        return Open;
    }

    public void setOpen(String open) {
        Open = open;
    }

    public String getOpenInterest() {
        return OpenInterest;
    }

    public void setOpenInterest(String openInterest) {
        OpenInterest = openInterest;
    }

    public String getQuotationLot() {
        return QuotationLot;
    }

    public void setQuotationLot(String quotationLot) {
        QuotationLot = quotationLot;
    }

    public String getSellPrice() {
        return SellPrice;
    }

    public void setSellPrice(String sellPrice) {
        SellPrice = sellPrice;
    }

    public String getSellQty() {
        return SellQty;
    }

    public void setSellQty(String sellQty) {
        SellQty = sellQty;
    }

    public String getTotalQtyTraded() {
        return TotalQtyTraded;
    }

    public void setTotalQtyTraded(String totalQtyTraded) {
        TotalQtyTraded = totalQtyTraded;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getPreOpen() {
        return PreOpen;
    }

    public void setPreOpen(String preOpen) {
        PreOpen = preOpen;
    }

    public String getPriceChange() {
        return PriceChange;
    }

    public void setPriceChange(String priceChange) {
        PriceChange = priceChange;
    }

    public String getPriceChangePercentage() {
        return PriceChangePercentage;
    }

    public void setPriceChangePercentage(String priceChangePercentage) {
        PriceChangePercentage = priceChangePercentage;
    }

    public String getOpenInterestChange() {
        return OpenInterestChange;
    }

    public void setOpenInterestChange(String openInterestChange) {
        OpenInterestChange = openInterestChange;
    }

    public String getMessageType() {
        return MessageType;
    }

    public void setMessageType(String messageType) {
        MessageType = messageType;
    }
}
