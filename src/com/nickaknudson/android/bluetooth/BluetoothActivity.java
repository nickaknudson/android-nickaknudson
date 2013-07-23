package com.nickaknudson.android.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public abstract class BluetoothActivity extends Activity {
	private static final String TAG = BluetoothActivity.class.getSimpleName();

    // Intent request codes
    public static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    public static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    public static final int REQUEST_ENABLE_BT = 3;
    public static final int REQUEST_DISCOVERABLE_BT = 4;
	
	private BluetoothAdapter bluetoothAdapter;
	
	public BluetoothAdapter getBluetoothAdapter() {
		return bluetoothAdapter;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get local Bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
       ensureBluetoothSupported();
    }

    @Override
    public void onStart() {
        super.onStart();
        // If Bluetooth is not on, request that it be enabled.
       ensureBluetoothEnabled();
    }

	public void ensureBluetoothSupported() {
        // If the adapter is null, then Bluetooth is not supported
        if (BluetoothAdapter.getDefaultAdapter() == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
        }
	}
	
	public void ensureBluetoothEnabled() {
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
        	onBluetoothEnabled();
        }
	}
	
	public void ensureDiscoverable() {
        if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivityForResult(discoverableIntent, REQUEST_DISCOVERABLE_BT);
        } else {
        	onDiscoverable();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE_SECURE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                String address = data.getStringExtra(BluetoothConnection.EXTRA_DEVICE_ADDRESS);
                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
                connectDevice(device, true);
            }
            break;
        case REQUEST_CONNECT_DEVICE_INSECURE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                String address = data.getStringExtra(BluetoothConnection.EXTRA_DEVICE_ADDRESS);
                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
                connectDevice(device, false);
            }
            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled
            	onBluetoothEnabled();
            } else {
                // User did not enable Bluetooth or an error occurred
                Log.d(TAG, "Bluetooth not enabled");
                Toast.makeText(this, "Please enable bluetooth.", Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
        case REQUEST_DISCOVERABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled
            	onDiscoverable();
            } else {
                // User did not enable Bluetooth or an error occurred
                Log.d(TAG, "Bluetooth not discoverable");
                Toast.makeText(this, "Please allow discovery.", Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
        }
    }

    public abstract void onBluetoothEnabled();
    public abstract void connectDevice(BluetoothDevice device, boolean secure);
    public abstract void onDiscoverable();
    
	public abstract void deviceListInsecure();
	public abstract void deviceListSecure();
}
