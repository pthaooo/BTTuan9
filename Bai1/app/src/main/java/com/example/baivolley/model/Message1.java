package com.example.baivolley.model;

import java.io.Serializable;
import java.util.List;

public class Message1 implements Serializable {
    private String message;
    private boolean success;
    private List<User> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<User> getResult() {
        return result;
    }

    public void setResult(List<User> result) {
        this.result = result;
    }
}
