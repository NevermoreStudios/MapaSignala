package com.nevermore.mapasignala;

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

public class SSUpdater extends PhoneStateListener {

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        if(PitajMeZaSignalStrength.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS)
        {
            PitajMeZaSignalStrength.setDbm(Integer.parseInt(signalStrength.toString().split(" ")[3]));
        }
        else if (PitajMeZaSignalStrength.getNetworkType() == TelephonyManager.NETWORK_TYPE_EDGE)
        {
            PitajMeZaSignalStrength.setDbm(Integer.parseInt(signalStrength.toString().split(" ")[1]));
        }

    }

}
