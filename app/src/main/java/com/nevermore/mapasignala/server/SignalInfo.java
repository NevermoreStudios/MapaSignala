package com.nevermore.mapasignala.server;

public class SignalInfo {
    public final int min;
    public final int max;
    public final float avg;

    public SignalInfo(int min, int max, float avg) {
        this.min = min;
        this.max = max;
        this.avg = avg;
    }
}
