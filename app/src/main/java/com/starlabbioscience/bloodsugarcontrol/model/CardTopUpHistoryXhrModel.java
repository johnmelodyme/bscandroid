package com.starlabbioscience.bloodsugarcontrol.model;

import androidx.annotation.NonNull;

import java.util.List;

public class CardTopUpHistoryXhrModel {
    private Boolean status;
    private String message;
    private List<CardTopUpHistoryRespondModel> data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CardTopUpHistoryRespondModel> getData() {
        return data;
    }

    public void setData(List<CardTopUpHistoryRespondModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public String toString() {
        return "CardTopUpHistoryXhrModel{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
