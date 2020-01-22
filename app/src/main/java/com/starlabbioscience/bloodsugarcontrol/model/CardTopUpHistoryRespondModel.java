package com.starlabbioscience.bloodsugarcontrol.model;

import androidx.annotation.NonNull;

public class CardTopUpHistoryRespondModel {
    private String duration;
    private String used_date;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getUsed_date() {
        return used_date;
    }

    public void setUsed_date(String used_date) {
        this.used_date = used_date;
    }

    public void clear() {
        duration = null;
        used_date = null;
    }

    @NonNull
    @Override
    public String toString() {
        return "CardTopUpHistoryRespondModel{" +
                "duration='" + duration + '\'' +
                ", used_date='" + used_date + '\'' +
                '}';
    }
}

