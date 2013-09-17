package com.nickaknudson.android.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public abstract class NetworkReceiver extends BroadcastReceiver {
	
	/*
	 <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
	 <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE"/>
	 <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
	 <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
	 */
	
	static public IntentFilter getFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		return filter;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// get the action
		String action = intent.getAction();
		// ConnectivityManager.CONNECTIVITY_ACTION
		if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			// API 17 - Integer networkType = intent.getIntExtra(ConnectivityManager.EXTRA_NETWORK_TYPE, Integer.MIN_VALUE);
			Boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
			onConnectivityAction(context, intent, noConnectivity);
			if(!noConnectivity) {
				ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				if (connectivityManager != null) {
					NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
					if(networkInfo != null) {
						// networkInfo.getState()
						switch(networkInfo.getState()) {
						case CONNECTED:
							onStateConnected(networkInfo);
							break;
						case CONNECTING:
							onStateConnecting(networkInfo);
							break;
						case DISCONNECTED:
							onStateDisconnected(networkInfo);
							break;
						case DISCONNECTING:
							onStateDisconnecting(networkInfo);
							break;
						case SUSPENDED:
							onStateSuspended(networkInfo);
							break;
						case UNKNOWN:
							onStateUnknown(networkInfo);
							break;
						}
					}
				}
			}
		}
		// WifiManager.NETWORK_STATE_CHANGED_ACTION
		if(action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
			NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			String bssid = intent.getStringExtra(WifiManager.EXTRA_BSSID);
			WifiInfo wifiInfo = (WifiInfo) intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
			onNetworkStateChanged(context, intent, networkInfo, bssid, wifiInfo);
		}
		// WifiManager.RSSI_CHANGED_ACTION
		if(action.equals(WifiManager.RSSI_CHANGED_ACTION)) {
			Integer rssi = intent.getIntExtra(WifiManager.EXTRA_NEW_RSSI, Integer.MIN_VALUE);
			onRssiChanged(context, intent, rssi);
		}
		// WifiManager.WIFI_STATE_CHANGED_ACTION
		if(action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
			Integer wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, Integer.MIN_VALUE);
			Integer prevWifiState = intent.getIntExtra(WifiManager.EXTRA_PREVIOUS_WIFI_STATE, Integer.MIN_VALUE);
			onWifiStateChangedAction(context, intent, wifiState, prevWifiState);
			// WifiManager.EXTRA_PREVIOUS_WIFI_STATE
			switch(wifiState) {
			case WifiManager.WIFI_STATE_ENABLED:
				onWifiStateEnabled(prevWifiState);
				break;
			case WifiManager.WIFI_STATE_ENABLING:
				onWifiStateEnabling(prevWifiState);
				break;
			case WifiManager.WIFI_STATE_DISABLED:
				onWifiStateDisabled(prevWifiState);
				break;
			case WifiManager.WIFI_STATE_DISABLING:
				onWifiStateDisabling(prevWifiState);
				break;
			case WifiManager.WIFI_STATE_UNKNOWN:
				onWifiStateUnknown(prevWifiState);
				break;
			}
		}
	}

	protected abstract void onConnectivityAction(Context context, Intent intent, Boolean noConnectivity);
	
	protected abstract void onNetworkStateChanged(Context context, Intent intent, NetworkInfo networkInfo, String bssid, WifiInfo wifiInfo);
	protected abstract void onStateUnknown(NetworkInfo networkInfo);
	protected abstract void onStateSuspended(NetworkInfo networkInfo);
	protected abstract void onStateDisconnecting(NetworkInfo networkInfo);
	protected abstract void onStateDisconnected(NetworkInfo networkInfo);
	protected abstract void onStateConnecting(NetworkInfo networkInfo);
	protected abstract void onStateConnected(NetworkInfo networkInfo);

	protected abstract void onRssiChanged(Context context, Intent intent, Integer rssi);
	
	protected abstract void onWifiStateChangedAction(Context context, Intent intent, Integer wifiState, Integer prevWifiState);
	protected abstract void onWifiStateEnabled(Integer prevWifiState);
	protected abstract void onWifiStateEnabling(Integer prevWifiState);
	protected abstract void onWifiStateDisabled(Integer prevWifiState);
	protected abstract void onWifiStateDisabling(Integer prevWifiState);
	protected abstract void onWifiStateUnknown(Integer prevWifiState);
}