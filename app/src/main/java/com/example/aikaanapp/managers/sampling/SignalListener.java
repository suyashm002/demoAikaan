package com.example.aikaanapp.managers.sampling;

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;

public class SignalListener extends PhoneStateListener {

    private int mGsmSignal = 0;
    private int mEvdoDbm = 0;
    private int mCdmaDbm = 0;

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        // TODO Auto-generated method stub
        super.onSignalStrengthsChanged(signalStrength);
        mGsmSignal = signalStrength.getGsmSignalStrength();
        mCdmaDbm = signalStrength.getCdmaDbm();
        mEvdoDbm = signalStrength.getEvdoDbm();
    }

    public int getmGsmSignal() {
        return mGsmSignal;
    }

    public int getmEvdoDbm() {
        return mEvdoDbm;
    }

    public int getmCdmaDbm() {
        return mCdmaDbm;
    }
}