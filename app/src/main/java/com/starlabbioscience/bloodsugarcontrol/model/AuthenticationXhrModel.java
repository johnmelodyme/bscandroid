package com.starlabbioscience.bloodsugarcontrol.model;

import androidx.annotation.NonNull;

public class AuthenticationXhrModel {
    private Boolean status;
    private String message;
    private String tokenValidDate;
    private DataAuthentication data;

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

    public DataAuthentication getData() {
        return data;
    }

    public void setData(DataAuthentication data) {
        this.data = data;
    }

    @NonNull
    @Override
    public String toString() {
        return "AuthenticationXhrModel{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
