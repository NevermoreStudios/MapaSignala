package com.nevermore.mapasignala;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.renderscript.RenderScript;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

public class PitajMeZaSignalStrength {

    static TelephonyManager telephonyManager;
    static FusedLocationProviderClient flc;
    static SSUpdater ssu;
    static int dbm;
    static Location loc;
    static LocationRequest locr;

    static boolean inited = false;

    static MainActivity act;

    @SuppressLint("MissingPermission")
    public static void init(MainActivity a) {
        if(!inited){
            act = a;
            telephonyManager = (TelephonyManager) a.getSystemService(Context.TELEPHONY_SERVICE);
            ssu = new SSUpdater();
            telephonyManager.listen(ssu, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
            locr = new LocationRequest();
            locr.setInterval(5000);
            locr.setPriority(PRIORITY_HIGH_ACCURACY);
            flc = LocationServices.getFusedLocationProviderClient(act);
            flc.requestLocationUpdates(locr, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    PitajMeZaSignalStrength.setLocation(locationResult.getLocations().get(0));
                    System.out.println("LOCUPD");
                }
            }, null);
            reqLocation();
            act.update();
            inited = true;
        }
    }

    @SuppressLint("MissingPermission")
    public static void reqLocation() {

        flc.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            PitajMeZaSignalStrength.setLocation(location);
                        }
                    }
                });
    }

    public static String getTSPName()
    {
        return telephonyManager.getNetworkOperatorName();
    }

    public static int getTSP()
    {
        return Integer.parseInt(telephonyManager.getNetworkOperator());
    }


    public static int getNetworkType()
    {
        return telephonyManager.getNetworkType();
    }

    public static void setDbm(int d)
    {
        // opseg -50 super -120 nema ga
        dbm=d;
        reqLocation();
        act.update();
    }

    public static int getDbm() {
        return dbm;
    }

    public static float getGeneration(int netType)
    {
        switch (netType)
        {
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
            default:return -1;
        }
    }

    public static String getStringType(int netType)
    {
        switch (netType)
        {
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
            default:return "";
        }
    }

    public static void setLocation(Location location) {
        PitajMeZaSignalStrength.loc = location;
        act.update();
    }
}