package com.example.appmainframe.Bean;

import android.widget.EditText;

public class EventMessage {
    private String message;

    public EventMessage(String message){
        this.message = message;
    }
    @Override
    public String toString() {
        return super.toString();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
