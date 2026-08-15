package com.aicustomersupport.demo.cs.dto;

import com.aicustomersupport.demo.cs.model.User;

public class Response {

    private int statusCode;
    private String message;
    private User user;

    public Response() {
    }

    public Response(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public Response(int statusCode, String message, User user) {
        this.statusCode = statusCode;
        this.message = message;
        this.user = user;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
