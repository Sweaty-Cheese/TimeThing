package com.example.timething.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Session {
    private int id;
    private double duration;
    private final LocalDateTime date;
    private final int job_id;
    private final LocalDateTime startTime;
    private final LocalDateTime finishTime;

    public Session (int id,
                    double duration,
                    LocalDateTime date,
                    LocalDateTime startTime,
                    LocalDateTime finishTime,
                    int job_id) {
        this.id = id;
        this.duration = duration;
        this.date = date;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.job_id = job_id;
    }

    public Session (
                    double duration,
                    LocalDateTime date,
                    LocalDateTime startTime,
                    LocalDateTime finishTime,
                    int job_id) {
        this.duration = duration;
        this.date = date;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.job_id = job_id;
    }

    public Session (
            LocalDateTime date,
            LocalDateTime startTime,
            LocalDateTime finishTime,
            int job_id) {
        this.date = date;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.job_id = job_id;
    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public double getDuration() {
        return duration;
    }
    public void setDuration(double duration) { this.duration = duration; }

    public LocalDateTime getDate() {
        return date;
    }

    public LocalDateTime getStartTime() { return startTime; }

    public LocalDateTime getFinishTime() { return finishTime; }

    public int getJob_id() {
        return job_id;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public String toString() {
        final DateTimeFormatter dtfHrsMins = DateTimeFormatter.ofPattern("hh:mm a");
        return date + "\n" +
                dtfHrsMins.format(startTime) + " - " + dtfHrsMins.format(finishTime) + "\n" +
                duration + " hrs.";
    }
}
