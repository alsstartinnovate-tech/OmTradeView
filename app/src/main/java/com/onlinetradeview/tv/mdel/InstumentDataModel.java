package com.onlinetradeview.tv.mdel;

public class InstumentDataModel {
    private String catId, exchangeName, instrumentIdentifier, expiryDate, instrumentName, quotationLot;
    private boolean isSelected = true;

    public InstumentDataModel(String catId, String exchangeName, String instrumentIdentifier, String expiryDate, String instrumentName, boolean isSelected, String quotationLot) {
        this.exchangeName = exchangeName;
        this.catId = catId;
        this.instrumentIdentifier = instrumentIdentifier;
        this.expiryDate = expiryDate;
        this.instrumentName = instrumentName;
        this.quotationLot = quotationLot;
        this.isSelected = isSelected;
    }

    public String getQuotationLot() {
        return quotationLot;
    }

    public void setQuotationLot(String quotationLot) {
        this.quotationLot = quotationLot;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getInstrumentIdentifier() {
        return instrumentIdentifier;
    }

    public void setInstrumentIdentifier(String instrumentIdentifier) {
        this.instrumentIdentifier = instrumentIdentifier;
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
}
