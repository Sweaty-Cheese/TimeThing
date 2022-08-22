package com.example.timething.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Session {
    private int id;
    private double duration;
    private final LocalDate date;
    private final int job_id;
    private final LocalTime startTime;
    private final LocalTime finishTime;

    public Session (int id,
                    double duration,
                    LocalDate date,
                    LocalTime startTime,
                    LocalTime finishTime,
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
                    LocalDate date,
                    LocalTime startTime,
                    LocalTime finishTime,
                    int job_id) {
        this.duration = duration;
        this.date = date;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.job_id = job_id;
    }

    public Session (
            LocalDate date,
            LocalTime startTime,
            LocalTime finishTime,
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

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() { return startTime; }

    public LocalTime getFinishTime() { return finishTime; }

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
