package com.starlabbioscience.bloodsugarcontrol.model;

import androidx.annotation.NonNull;

public class CardTopUpModel {
    private String id;
    private String topUpCode;
    private String topUpTransactionDate;
    private String topUpStartDate;
    private String topUpEndDate;
    private String topUpDay;
    private String topUpPrice;
    private String topUpCurrencyCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopUpCode() {
        return topUpCode;
    }

    public void setTopUpCode(String topUpCode) {
        this.topUpCode = topUpCode;
    }

    public String getTopUpTransactionDate() {
        return topUpTransactionDate;
    }

    public void setTopUpTransactionDate(String topUpTransactionDate) {
        this.topUpTransactionDate = topUpTransactionDate;
    }

    public String getTopUpStartDate() {
        return topUpStartDate;
    }

    public void setTopUpStartDate(String topUpStartDate) {
        this.topUpStartDate = topUpStartDate;
    }

    public String getTopUpEndDate() {
        return topUpEndDate;
    }

    public void setTopUpEndDate(String topUpEndDate) {
        this.topUpEndDate = topUpEndDate;
    }

    public String getTopUpDay() {
        return topUpDay;
    }

    public void setTopUpDay(String topUpDay) {
        this.topUpDay = topUpDay;
    }

    public String getTopUpPrice() {
        return topUpPrice;
    }

    public void setTopUpPrice(String topUpPrice) {
        this.topUpPrice = topUpPrice;
    }

    public String getTopUpCurrencyCode() {
        return topUpCurrencyCode;
    }

    public void setTopUpCurrencyCode(String topUpCurrencyCode) {
        this.topUpCurrencyCode = topUpCurrencyCode;
    }

    private void clear() {
        id = null;
        topUpCode = null;
        topUpTransactionDate = null;
        topUpStartDate = null;
        topUpEndDate = null;
        topUpDay = null;
        topUpPrice = null;
        topUpCurrencyCode = null;
    }

    @NonNull
    @Override
    public String toString() {
        return "CardTopUpModel{" +
                "id='" + id + '\'' +
                ", topUpCode='" + topUpCode + '\'' +
                ", topUpTransactionDate='" + topUpTransactionDate + '\'' +
                ", topUpStartDate='" + topUpStartDate + '\'' +
                ", topUpEndDate='" + topUpEndDate + '\'' +
                ", topUpDay='" + topUpDay + '\'' +
                ", topUpPrice='" + topUpPrice + '\'' +
                ", topUpCurrencyCode='" + topUpCurrencyCode + '\'' +
                '}';
    }
}

