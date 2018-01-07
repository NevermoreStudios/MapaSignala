package com.nevermore.mapasignala.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.nevermore.mapasignala.server.SignalData;

@Entity
public class Entry {
    @PrimaryKey(autoGenerate = true)
    public int ID;
    @ColumnInfo(name = "lat")
    public double latitude;
    @ColumnInfo(name = "lon")
    public double longitude;
    @ColumnInfo(name = "dbm")
    public int dbm;
    @ColumnInfo(name = "type")
    public int type;
    @ColumnInfo(name = "provider")
    public int provider;

    public Entry(double latitude, double longitude, int dbm, int type, int provider) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.dbm = dbm;
        this.type = type;
        this.provider = provider;
    }

    public SignalData getSignalData() {
        return new SignalData( latitude,  longitude,  dbm,  type,  provider);
    }
}
