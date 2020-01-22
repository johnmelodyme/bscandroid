package com.starlabbioscience.bloodsugarcontrol.model;

import androidx.annotation.NonNull;

public class CardTopUpXhrWrongModel {
    private Boolean status;
    private String message;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
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