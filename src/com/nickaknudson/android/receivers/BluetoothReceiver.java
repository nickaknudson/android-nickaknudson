package com.nickaknudson.android.receivers;

import java.util.LinkedList;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelUuid;
import android.os.Parcelable;

public abstract class BluetoothReceiver extends BroadcastReceiver {
	
	/*
	 <uses-permission android:name="android.permission.BLUETOOTH"/>
     <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
	 */
	
	static public IntentFilter getFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
		filter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothDevice.ACTION_UUID);
		//TODO filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
		//TODO filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		//TODO filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
		//TODO filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		//TODO filter.addAction(BluetoothDevice.ACTION_CLASS_CHANGED);
		//TODO filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
		return filter;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// get the action
		String action = intent.getAction();
		// BluetoothAdapter.ACTION_STATE_CHANGED
        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
			Integer state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
			Integer prevState = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE, BluetoothAdapter.ERROR);
			onStateChanged(context, intent, state, prevState);
			// BluetoothAdapter.EXTRA_STATE
			switch(state) {
			case BluetoothAdapter.STATE_ON:
				onStateOn(prevState);
				break;
			case BluetoothAdapter.STATE_OFF:
				onStateOff(prevState);
				break;
			case BluetoothAdapter.STATE_TURNING_ON:
				onStateTurningOn(prevState);
				break;
			case BluetoothAdapter.STATE_TURNING_OFF:
				onStateTurningOff(prevState);
				break;
			case BluetoothAdapter.ERROR:
				onError(context, intent);
				break;
			}
        }
		// BluetoothAdapter.ACTION_SCAN_MODE_CHANGED
        if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
			Integer scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);
			Integer prevScanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_SCAN_MODE, BluetoothAdapter.ERROR);
			onScanModeChanged(context, intent, scanMode, prevScanMode);
			// BluetoothAdapter.EXTRA_SCAN_MODE
			switch(scanMode) {
			case BluetoothAdapter.SCAN_MODE_NONE:
				onScanModeNone(prevScanMode);
				break;
			case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
				onScanModeConnectable(prevScanMode);
				break;
			case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
				onScanModeConnectableDiscoverable(prevScanMode);
				break;
			case BluetoothAdapter.ERROR:
				onError(context, intent);
				break;
			}
        }
		// BluetoothAdapter.ACTION_DISCOVERY_STARTED
        if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
        	onDiscoveryStarted(context, intent);
        }
		// BluetoothAdapter.ACTION_DISCOVERY_FINISHED
        if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
        	onDiscoveryFinished(context, intent);
        }
		// BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED
        if (action.equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			Integer connectionState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, BluetoothAdapter.ERROR);
			Integer prevConnectionState = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_CONNECTION_STATE, BluetoothAdapter.ERROR);
			onConnectionStateChanged(context, intent, device, connectionState, prevConnectionState);
			// BluetoothAdapter.EXTRA_CONNECTION_STATE
			switch(connectionState) {
			case BluetoothAdapter.STATE_CONNECTED:
				onStateConnected(device, prevConnectionState);
				break;
			case BluetoothAdapter.STATE_DISCONNECTED:
				onStateDisconnected(device, prevConnectionState);
				break;
			case BluetoothAdapter.STATE_CONNECTING:
				onStateConnecting(device, prevConnectionState);
				break;
			case BluetoothAdapter.STATE_DISCONNECTING:
				onStateDisconnecting(device, prevConnectionState);
				break;
			case BluetoothAdapter.ERROR:
				onError(context, intent);
				break;
			}
        }
		// BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED
        if (action.equals(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED)) {
        	String name = intent.getStringExtra(BluetoothAdapter.EXTRA_LOCAL_NAME);
        	onLocalNameChanged(context, intent, name);
        }
		// BluetoothDevice.ACTION_FOUND
        if (action.equals(BluetoothDevice.ACTION_FOUND)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Short RSSI = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
            onFound(context, intent, device, RSSI);
        }
		// BluetoothDevice.ACTION_UUID
        if (action.equals(BluetoothDevice.ACTION_UUID)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Parcelable[] puuids = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
            LinkedList<UUID> uuids = new LinkedList<UUID>();
            if(puuids != null && puuids.length > 0) {
	            for(Parcelable puuid : puuids) {
	            	UUID uuid = ((ParcelUuid) puuid).getUuid();
	            	uuids.add(uuid);
	            }
            }
            onUUID(context, intent, device, uuids);
        }
	}
	
	public abstract void onStateChanged(Context context, Intent intent, Integer state, Integer prevState);
	public abstract void onStateOn(Integer prevState);
	public abstract void onStateOff(Integer prevState);
	public abstract void onStateTurningOn(Integer prevState);
	public abstract void onStateTurningOff(Integer prevState);
	
	public abstract void onScanModeChanged(Context context, Intent intent, Integer scanMode, Integer prevScanMode);
	public abstract void onScanModeNone(Integer prevScanMode);
	public abstract void onScanModeConnectable(Integer prevScanMode);
	public abstract void onScanModeConnectableDiscoverable(Integer prevScanMode);
	
	public abstract void onDiscoveryStarted(Context context, Intent intent);
	public abstract void onDiscoveryFinished(Context context, Intent intent);
	
	public abstract void onConnectionStateChanged(Context context, Intent intent, BluetoothDevice device, Integer connectionState, Integer prevConnectionState);
	public abstract void onStateConnected(BluetoothDevice device, Integer prevConnectionState);
	public abstract void onStateDisconnected(BluetoothDevice device, Integer  prevConnectionState);
	public abstract void onStateConnecting(BluetoothDevice device, Integer  prevState);
	public abstract void onStateDisconnecting(BluetoothDevice device, Integer  prevState);
	
	public abstract void onLocalNameChanged(Context context, Intent intent, String name);
	
	public abstract void onFound(Context context, Intent intent, BluetoothDevice device, Short RSSI);
	public abstract void onUUID(Context context, Intent intent, BluetoothDevice device, LinkedList<UUID> uuids);
	
	public abstract void onError(Context context, Intent intent);
}
