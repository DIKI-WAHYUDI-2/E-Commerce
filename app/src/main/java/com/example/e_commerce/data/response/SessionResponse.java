package com.example.e_commerce.data.response;

import java.util.List;

public class SessionResponse {

    private List<Session> data;
    private String message;
    private int status;

    public SessionResponse(List<Session> data, String message, int status) {
        this.data = data;
        this.message = message;
        this.status = status;
    }

    public List<Session> getData() {
        return data;
    }

    public void setData(List<Session> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
