package com.starlabbioscience.bloodsugarcontrol.model;

import androidx.annotation.NonNull;

public class CardTopUpXhrModel {
    private Boolean status;
    private String message;
    private CardTopUpRespondModel data;

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

    public CardTopUpRespondModel getData() {
        return data;
    }

    public void setData(CardTopUpRespondModel data) {
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
