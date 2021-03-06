package com.nevermore.mapasignala.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

class SignalStrength {

    private static TelephonyManager telephonyManager;
    @SuppressLint("StaticFieldLeak")
    private static FusedLocationProviderClient flc;
    private static int dbm;
    static Location loc;
    private static boolean initialized = false;
    @SuppressLint("StaticFieldLeak")
    private static StatusActivity act;

    @SuppressLint("MissingPermission")
    static void init(Activity a) {
        if (!initialized) {
            telephonyManager = (TelephonyManager) a.getSystemService(Context.TELEPHONY_SERVICE);
            SSUpdater ssu = new SSUpdater();
            telephonyManager.listen(ssu, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(5000);
            locationRequest.setPriority(PRIORITY_HIGH_ACCURACY);
            flc = LocationServices.getFusedLocationProviderClient(a);
            flc.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    SignalStrength.setLocation(locationResult.getLocations().get(0));
                }
            }, null);
            reqLocation();
            initialized = true;
        }
    }

    static void setActivity(StatusActivity activity) {
        act = activity;
    }

    @SuppressLint("MissingPermission")
    private static void reqLocation() {
        flc.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    SignalStrength.setLocation(location);
                }
            }
        });
    }

    static String getTSPName() {
        return telephonyManager.getNetworkOperatorName();
    }

    static int getTSP() {
        return Integer.parseInt(telephonyManager.getNetworkOperator());
    }


    static int getNetworkType() {
        return telephonyManager.getNetworkType();
    }

    static void setDbm(int d) {
        // opseg -50 super -120 nema ga
        dbm = d;
        reqLocation();
        if (act != null) {
            act.update();
        }
    }

    static int getDbm() {
        return dbm;
    }

    static float getGeneration(int netType) {
        switch (netType) {
            case TelephonyManager.NETWORK_TYPE_EDGE: return 2.5f;
            case TelephonyManager.NETWORK_TYPE_GPRS: return 2.5f;
            case TelephonyManager.NETWORK_TYPE_GSM: return 2;
            case TelephonyManager.NETWORK_TYPE_HSDPA: return 3.5f;
            case TelephonyManager.NETWORK_TYPE_HSPA: return 3.5f;
            case TelephonyManager.NETWORK_TYPE_HSUPA: return 3.5f;
            case TelephonyManager.NETWORK_TYPE_LTE: return 4f;
            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:return 3;
            case TelephonyManager.NETWORK_TYPE_UMTS: return 3;
            case TelephonyManager.NETWORK_TYPE_HSPAP: return 3.5f;
            default: return -1;
        }
    }

    static String getStringType(int netType) {
        switch (netType) {
            case TelephonyManager.NETWORK_TYPE_EDGE: return "EDGE";
            case TelephonyManager.NETWORK_TYPE_GPRS: return "GPRS";
            case TelephonyManager.NETWORK_TYPE_GSM: return "GSM";
            case TelephonyManager.NETWORK_TYPE_HSDPA: return "HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSPA: return "HSPA";
            case TelephonyManager.NETWORK_TYPE_HSUPA: return "HSUPA";
            case TelephonyManager.NETWORK_TYPE_LTE: return "LTE";
            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:return "SCDMA";
            case TelephonyManager.NETWORK_TYPE_UMTS: return "UMTS";
            case TelephonyManager.NETWORK_TYPE_HSPAP: return "HSPA+";
            default: return "";
        }
    }

    private static void setLocation(Location location) {
        loc = location;
        if (act != null){
            act.update();
        }
    }
}