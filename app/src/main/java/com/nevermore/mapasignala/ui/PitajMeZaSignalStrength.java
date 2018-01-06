package com.nevermore.mapasignala.ui;

import android.app.Activity;
import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class PitajMeZaSignalStrength {

    static TelephonyManager telephonyManager;
    static SSUpdater ssu;
    static int dbm;

    public static void init(Activity a)
    {
        telephonyManager = (TelephonyManager)a.getSystemService(Context.TELEPHONY_SERVICE);
        ssu = new SSUpdater();
        telephonyManager.listen(ssu, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    public static String getTSPName()
    {
        return telephonyManager.getNetworkOperatorName();
    }

    public static int getNetworkType()
    {
        return telephonyManager.getNetworkType();
    }

    public static void setDbm(int d)
    {
        // opseg -50 super -120 nema ga
        dbm=d;
        System.out.println(getGeneration(PitajMeZaSignalStrength.getNetworkType())+"G");
        System.out.println(d+"dBm");
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
            case TelephonyManager.NETWORK_TYPE_LTE: return 3.5f;
            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:return 3;
            case TelephonyManager.NETWORK_TYPE_UMTS: return 3;
            default:return -1;
        }
    }
}