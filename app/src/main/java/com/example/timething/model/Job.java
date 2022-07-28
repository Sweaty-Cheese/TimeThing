package com.example.timething.model;

public class Job {
    private int id;
    private String name;
    private int client_id;
    private int job_completed;

    public Job (){}

    public Job (int id, String name, int client_id, int job_completed) {
        this.id = id;
        this.name = name;
        this.client_id = client_id;
        this.job_completed = job_completed;
    }

    public Job (String name, int client_id, int job_completed) {
        this.name = name;
        this.client_id = client_id;
        this.job_completed = job_completed;
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

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int isJob_completed() {
        return job_completed;
    }

    public void setJob_completed(int job_completed) {
        this.job_completed = job_completed;
    }

    @Override
    public String toString() { return name; }
}
