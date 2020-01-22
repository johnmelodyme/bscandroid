package com.starlabbioscience.bloodsugarcontrol.model;

import androidx.annotation.NonNull;

import io.realm.RealmObject;

public class UserModel extends RealmObject {
    private String id;
    private String first_name;
    private String email;
    private String phone;
    private String valid_date;
    private String status;
    private String token;
    private String tokenValidDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getValid_date() {
        return valid_date;
    }

    public void setValid_date(String valid_date) {
        this.valid_date = valid_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenValidDate() {
        return tokenValidDate;
    }

    public void setTokenValidDate(String tokenValidDate) {
        this.tokenValidDate = tokenValidDate;
    }

    public void clear() {
        id = null;
        first_name = null;
        email = null;
        phone = null;
        valid_date = null;
        status = null;
        token = null;
        tokenValidDate = null;
    }

    @NonNull
    @Override
    public String toString() {
        return "DataAuthentication{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", valid_date='" + valid_date + '\'' +
                ", status=" + status +
                ", token='" + token + '\'' +
                ", tokenValidDate='" + tokenValidDate + '\'' +

                '}';
    }
}
