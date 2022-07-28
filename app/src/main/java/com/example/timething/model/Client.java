package com.example.timething.model;

import androidx.annotation.NonNull;

public class Client {
    private int id;
    private String name;

    public Client (){}

    public Client (int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
