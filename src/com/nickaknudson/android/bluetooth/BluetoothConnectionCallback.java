package com.nickaknudson.android.bluetooth;

public interface BluetoothConnectionCallback {
	public void onConnection(BluetoothConnection connection);
	public void onFailed(BluetoothConnection connection);
	public void onLost(BluetoothConnection connection);
	public void onRecieve(int bytes, byte[] buffer);
}
