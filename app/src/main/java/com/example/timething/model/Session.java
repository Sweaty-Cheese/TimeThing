package com.example.timething.model;

public class Session {
    private int id;
    private final double duration;
    private final String date;
    private final int job_id;

    public Session (int id,
                    double duration,
                    String date,
                    int job_id) {
        this.id = id;
        this.duration = duration;
        this.date = date;
        this.job_id = job_id;
    }

    public Session (
                    double duration,
                    String date,
                    int job_id) {
        this.duration = duration;
        this.date = date;
        this.job_id = job_id;
    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public double getDuration() {
        return duration;
    }

    public String getDate() {
        return date;
    }

    public int getJob_id() {
        return job_id;
    }

    @Override
    public String toString() {
        return date + "\n" + duration + " hrs.";
    }
}
