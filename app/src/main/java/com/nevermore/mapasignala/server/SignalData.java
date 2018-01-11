package com.nevermore.mapasignala.server;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class SignalData {
    private final double latitude;
    private final double longitude;
    private final int dbm;
    private final int type;
    private final int provider;

    public SignalData(double latitude, double longitude, int dbm, int type, int provider) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.dbm = dbm;
        this.type = type;
        this.provider = provider;
    }
}
