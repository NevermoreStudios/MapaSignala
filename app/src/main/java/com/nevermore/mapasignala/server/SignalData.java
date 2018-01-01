package com.nevermore.mapasignala.server;

public class SignalData {
    public final double latitude;
    public final double longitude;
    public final int dbm;
    public final int type;
    public final int provider;

    public SignalData(double latitude, double longitude, int dbm, int type, int provider) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.dbm = dbm;
        this.type = type;
        this.provider = provider;
    }
}
