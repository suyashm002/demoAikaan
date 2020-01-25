package com.example.aikaanapp.models;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import com.example.aikaanapp.models.data.CellInfo;

import static com.example.aikaanapp.models.Phone.PHONE_TYPE_CDMA;
import static com.example.aikaanapp.models.Phone.PHONE_TYPE_GSM;

/**
 * Gsm.
 */
public class Gsm {

    private static final String TAG = "Gsm";

    /* check the GSM cell information */
    public static CellInfo getCellInfo(Context context) {
        CellInfo cellInfo = new CellInfo();

        TelephonyManager manager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);

        String netOperator = manager.getNetworkOperator();

        // Fix crash when not connected to network (airplane mode, underground,
        // etc)
        if (netOperator == null || netOperator.length() < 3) {
            return cellInfo;
        }

        /*
         * FIXME: Actually check for mobile network status == connected before
         * doing this stuff.
         */

        if (Phone.getType(context).equals(PHONE_TYPE_CDMA)) {
            CdmaCellLocation cdmaLocation = (CdmaCellLocation) manager.getCellLocation();

            cellInfo.cid = cdmaLocation.getBaseStationId();
            cellInfo.lac = cdmaLocation.getNetworkId();
            cellInfo.mnc = cdmaLocation.getSystemId();
            cellInfo.mcc = Integer.parseInt(netOperator.substring(0, 3));
            cellInfo.radioType = Network.getMobileNetworkType(context);
        } else if (Phone.getType(context).equals(PHONE_TYPE_GSM)) {
            GsmCellLocation gsmLocation = (GsmCellLocation) manager.getCellLocation();

            cellInfo.mcc = Integer.parseInt(netOperator.substring(0, 3));
            cellInfo.mnc = Integer.parseInt(netOperator.substring(3));
            cellInfo.lac = gsmLocation.getLac();
            cellInfo.cid = gsmLocation.getCid();
            cellInfo.radioType = Network.getMobileNetworkType(context);
        }

        return cellInfo;
    }
}