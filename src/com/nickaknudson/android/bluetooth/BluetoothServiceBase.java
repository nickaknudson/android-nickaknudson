package com.nickaknudson.android.bluetooth;

import java.util.LinkedList;
import java.util.UUID;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.util.Log;


public abstract class BluetoothServiceBase extends Service {
	private static final String TAG = BluetoothServiceBase.class.getSimpleName();
	
	private BluetoothAdapter bluetoothAdapter;

	public BluetoothAdapter getBluetoothAdapter() {
		return bluetoothAdapter;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate");
		
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter == null) {
		    // Device does not support Bluetooth
		} else {
			// register broadcast receivers
			registerStateChangedReceiver();
			registerScanModeChangedReceiverChangedReceiver();
			registerDiscoveryStartedReceiver();
			registerDiscoveryFinishedReceiver();
			registerConnectionStateChangedReceiver();
			registerDeviceFoundReceiver();
			registerDeviceUUIDReceiver();
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(bluetoothAdapter.isEnabled()) {
			onStateOn(null);
		}
		return 0;
	}

	@Override
	public IBinder onBind(Intent intent) {
		if(bluetoothAdapter.isEnabled()) {
			onStateOn(null);
		}
		return null;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");

		// unregister broadcast receivers
		unregisterStateChangedReceiver();
		unregisterScanModeChangedReceiverChangedReceiver();
		unregisterDiscoveryStartedReceiver();
		unregisterDiscoveryFinishedReceiver();
		unregisterConnectionStateChangedReceiver();
		unregisterDeviceFoundReceiver();
		unregisterDeviceUUIDReceiver();
	}
	
    /****************************************
     *  State Changed Receiver
     ****************************************/    
	private void registerStateChangedReceiver() {
		IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
		registerReceiver(stateChangedReceiver, filter);
	}
	
	private void unregisterStateChangedReceiver() {
		unregisterReceiver(stateChangedReceiver);
	}
	
	private BroadcastReceiver stateChangedReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
				Integer state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
				Integer prevState = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE, BluetoothAdapter.STATE_OFF);
				onReceiveStateChanged(context, intent, state, prevState);
				
				switch(state) {
				case BluetoothAdapter.STATE_ON:
					onStateOn(prevState);
					break;
				case BluetoothAdapter.STATE_OFF:
					onStateOff(prevState);
					break;
				case BluetoothAdapter.STATE_CONNECTING:
					onStateConnecting(prevState);
					break;
				case BluetoothAdapter.STATE_DISCONNECTING:
					onStateDisconnecting(prevState);
					break;
				}
	        }
		}
	};
	
	protected abstract void onReceiveStateChanged(Context context, Intent intent, Integer state, Integer prevState);
	protected abstract void onStateOn(Integer prevState);
	protected abstract void onStateOff(Integer prevState);
	protected abstract void onStateConnecting(Integer prevState);
	protected abstract void onStateDisconnecting(Integer prevState);
	
    /****************************************
     *  Scan Mode Changed Receiver
     ****************************************/    
	private void registerScanModeChangedReceiverChangedReceiver() {
		IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		registerReceiver(scanModeChangedReceiver, filter);
	}
	
	private void unregisterScanModeChangedReceiverChangedReceiver() {
		unregisterReceiver(scanModeChangedReceiver);
	}
	
	private BroadcastReceiver scanModeChangedReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
				Integer scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.SCAN_MODE_NONE);
				Integer prevScanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_SCAN_MODE, BluetoothAdapter.SCAN_MODE_NONE);
				onReceiveScanModeChanged(context, intent, scanMode, prevScanMode);
	        }
		}
	};
	
	protected abstract void onReceiveScanModeChanged(Context context, Intent intent, Integer scanMode, Integer prevScanMode);
	
    /****************************************
     *  Discovery Started Receiver
     ****************************************/    
	private void registerDiscoveryStartedReceiver() {
		IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		registerReceiver(discoveryStartedReceiver, filter);
	}
	
	private void unregisterDiscoveryStartedReceiver() {
		unregisterReceiver(discoveryStartedReceiver);
	}
	
	private BroadcastReceiver discoveryStartedReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
	        	onReceiveDiscoveryStarted(context, intent);
	        }
		}
	};
	
	protected abstract void onReceiveDiscoveryStarted(Context context, Intent intent);
	
    /****************************************
     *  Discovery Finished Receiver
     ****************************************/    
	private void registerDiscoveryFinishedReceiver() {
		IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(discoveryFinishedReceiver, filter);
	}
	
	private void unregisterDiscoveryFinishedReceiver() {
		unregisterReceiver(discoveryFinishedReceiver);
	}
	
	private BroadcastReceiver discoveryFinishedReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
	        	onReceiveDiscoveryFinished(context, intent);
	        }
		}
	};
	
	protected abstract void onReceiveDiscoveryFinished(Context context, Intent intent);
	
    /****************************************
     *  Connection State Changed Receiver
     ****************************************/    
	private void registerConnectionStateChangedReceiver() {
		IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
		registerReceiver(connectionStateChangedReceiver, filter);
	}
	
	private void unregisterConnectionStateChangedReceiver() {
		unregisterReceiver(connectionStateChangedReceiver);
	}
	
	private BroadcastReceiver connectionStateChangedReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        if (action.equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)) {
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Integer connectionState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, BluetoothAdapter.STATE_DISCONNECTED);
				Integer prevConnectionState = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_CONNECTION_STATE, BluetoothAdapter.STATE_DISCONNECTED);
				onReceiveConnectionStateChanged(context, intent, device, connectionState, prevConnectionState);
	        }
		}
	};
	
	protected abstract void onReceiveConnectionStateChanged(Context context, Intent intent, BluetoothDevice device, Integer connectionState, Integer prevConnectionState);
	
    /****************************************
     *  Device Found Receiver
     ****************************************/    
	private void registerDeviceFoundReceiver() {
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(deviceFoundReceiver, filter);
	}
	
	private void unregisterDeviceFoundReceiver() {
		unregisterReceiver(deviceFoundReceiver);
	}
	
	private BroadcastReceiver deviceFoundReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        if (action.equals(BluetoothDevice.ACTION_FOUND)) {
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            Short RSSI = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
	            onReceiveDeviceFound(context, intent, device, RSSI);
	        }
		}
	};
	
	protected abstract void onReceiveDeviceFound(Context context, Intent intent, BluetoothDevice device, Short RSSI);
	
    /****************************************
     *  Device UUID Receiver
     ****************************************/    
	private void registerDeviceUUIDReceiver() {
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_UUID);
		registerReceiver(deviceUUIDReceiver, filter);
	}
	
	private void unregisterDeviceUUIDReceiver() {
		unregisterReceiver(deviceUUIDReceiver);
	}
	
	private BroadcastReceiver deviceUUIDReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
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
	            onReceiveUUID(context, intent, device, uuids);
	        }
		}
	};
	
	protected abstract void onReceiveUUID(Context context, Intent intent, BluetoothDevice device, LinkedList<UUID> uuids);
}
