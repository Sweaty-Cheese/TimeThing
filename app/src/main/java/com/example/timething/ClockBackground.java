package com.example.timething;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ClockBackground {
    public static boolean clockRunning = false;
    public static long timeAway = 0;
    public static long pauseDuration = 0;
    public static LocalTime startTime = null;
    public static LocalTime pauseTime = null;
}
