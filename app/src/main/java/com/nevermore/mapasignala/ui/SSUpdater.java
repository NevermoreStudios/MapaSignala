package com.nevermore.mapasignala.ui;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class SSUpdater extends PhoneStateListener {

    @Override
    public void onSignalStrengthsChanged(android.telephony.SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        System.out.println(SignalStrength.getNetworkType());
        int index = getIndex(SignalStrength.getNetworkType());
        if (index == -1) {
            System.out.println("Unknown network type");
        } else {
            SignalStrength.setDbm(Integer.parseInt(signalStrength.toString().split(" ")[index]));
        }
    }

    private int getIndex(int type) {
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return 1;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return 3;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return 11;
            default: return -1;
        }
    }

}
