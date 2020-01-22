package com.starlabbioscience.bloodsugarcontrol.model;

import androidx.annotation.NonNull;

public class ForgetXhrModel {
    private String status;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public void clear(){
        status = null;
        message = null;
    }

    @NonNull
    @Override
    public String toString() {
        return "ForgetXhrModel{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
