package com.nevermore.mapasignala.server;

public class SignalInfo {
    public final int min;
    public final int max;
    public final int avg;

    public SignalInfo(int min, int max, int avg) {
        this.min = min;
        this.max = max;
        this.avg = avg;
    }
}
