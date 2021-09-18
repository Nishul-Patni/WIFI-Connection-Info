package com.example.wifilteinformation;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Locale;

public class Tab2 extends Fragment {

    WifiManager wm;
    WifiInfo wifiInfo;
    ConnectivityStatusReceiver cr;
    ConnectivityStatusReceiver cr2;

    TextView wifi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wm = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        cr = new ConnectivityStatusReceiver();
        cr2 = new ConnectivityStatusReceiver();
        IntentFilter filter = new IntentFilter(WifiManager.RSSI_CHANGED_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        getActivity().registerReceiver(cr, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);
        wifi = view.findViewById(R.id.WIFIDetails);

        ConnectivityManager connMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            wifiInfo = wm.getConnectionInfo();
            String details = getWIFIDetails();
            wifi.setText(details);
        }else{
            wifi.setText("For Wifi related details\n Please connect to WIFI");
        }

        return view;
    }

    public String getWIFIDetails(){
        String details = "SSID: "+ wifiInfo.getSSID()
                +"\nBSSID: "+wifiInfo.getBSSID().toUpperCase(Locale.ROOT)
                +"\nHSSID: "+wifiInfo.getHiddenSSID()
                +"\nIP Address: " + Formatter.formatIpAddress(wifiInfo.getIpAddress())
                +"\nRSSI: "+wifiInfo.getRssi()
                +"\nLink Speed: "+wifiInfo.getLinkSpeed()
                +"\nNetwork ID: "+wifiInfo.getNetworkId()
                +"\nSignal Strength: "+getWifiSignalStrength(WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 6));

        return details;
    }

    private String getWifiSignalStrength(int level) {

        if (level >4) {
            return "Good";
        } else if (level >= 3) {
            return "Average";
        } else if (level == 2) {
            return "Weak";
        } else{
            return "Very Weak";
        }
    }

    class ConnectivityStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null && activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                wifiInfo = wm.getConnectionInfo();
                String details = getWIFIDetails();
                wifi.setText(details);
            } else {
                wifi.setText("For Wifi related details\n Please connect to WIFI");
            };
            getFragmentManager().beginTransaction().detach(Tab2.this).attach(Tab2.this).commit();
        }
    }
}