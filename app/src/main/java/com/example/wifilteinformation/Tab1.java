package com.example.wifilteinformation;

import static android.telephony.TelephonyManager.NETWORK_TYPE_1xRTT;
import static android.telephony.TelephonyManager.NETWORK_TYPE_CDMA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_B;
import static android.telephony.TelephonyManager.NETWORK_TYPE_GSM;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSPAP;
import static android.telephony.TelephonyManager.NETWORK_TYPE_IDEN;
import static android.telephony.TelephonyManager.NETWORK_TYPE_LTE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_NR;
import static android.telephony.TelephonyManager.NETWORK_TYPE_TD_SCDMA;
import static com.example.wifilteinformation.R.layout.fragment_tab1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;


public class Tab1 extends Fragment {

    TextView lte;
    TelephonyManager mTelephonyManager;
    MyPhoneStateListener mPhoneStatelistener;
    Fragment currentFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPhoneStatelistener = new MyPhoneStateListener();
        mTelephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(mPhoneStatelistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getLteDetails() {
        TelephonyManager manager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "Permissions not granted";
        }
        System.out.println(manager.getNetworkOperatorName());
        String details = "Carrier Name: " + manager.getNetworkOperatorName()
                + "\nNetwork Type: " + getNetworkType(manager.getNetworkType())
                + "\nMCC: " + manager.getNetworkOperator().substring(0, 3)
                + "\nMNC: " + manager.getNetworkOperator().substring(3)
                + "\nIMEI/SV: " + manager.getDeviceSoftwareVersion();
        return details;
    }

    public static String getNetworkType(int type){
        Log.d("hey", "getNetworkType: "+type+" "+NETWORK_TYPE_IDEN);
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
            case TelephonyManager.NETWORK_TYPE_NR:
                return "5G";
            default:
                return "Unknown";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(fragment_tab1, container, false);
        lte = view.findViewById(R.id.LTEDetails);
        return view;
    }

    class MyPhoneStateListener extends PhoneStateListener {

        public int signalSupport = 0;

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            String details = getLteDetails();
            String tag = "";
            signalSupport = signalStrength.getGsmSignalStrength();

            if (signalSupport > 30) {
                tag = "Good";
            } else if (signalSupport > 20 && signalSupport < 30) {
                tag = "Average";
            } else if (signalSupport < 20 && signalSupport > 3) {
                tag = "Weak";
            } else if (signalSupport < 3) {
                tag = "Very Weak";
            }
            details+="\nSignal Strength: "+tag;
            lte.setText(details);
            getFragmentManager().beginTransaction().detach(Tab1.this).attach(Tab1.this).commit();
        }
    }
}

















