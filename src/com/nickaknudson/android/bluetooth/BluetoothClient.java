package com.nickaknudson.android.bluetooth;

import java.lang.reflect.Type;

import android.bluetooth.BluetoothDevice;
import com.google.gson.Gson;
import com.nickaknudson.mva.Model;
import com.nickaknudson.mva.callbacks.PersistentCallback;
import com.nickaknudson.mva.callbacks.PersistentCallbackManager;
import com.nickaknudson.mva.callbacks.ReceiveCallback;
import com.nickaknudson.mva.callbacks.ReceiveCallbackManager;
import com.nickaknudson.mva.callbacks.SendCallback;
import com.nickaknudson.mva.clients.PersistentClient;
import com.nickaknudson.mva.clients.SRClient;

public abstract class BluetoothClient<T extends Model<T>> implements SRClient<T>, PersistentClient {

	private BluetoothDevice device;
	private boolean secure;
	private BluetoothConnection bluetoothConnection;
	protected boolean connected = false;
	private Gson gson;
	private PersistentCallbackManager pcallbacks;
	private ReceiveCallbackManager<T> rcallbacks;

	public BluetoothClient(BluetoothDevice device, boolean secure) {
		pcallbacks = new PersistentCallbackManager();
		rcallbacks = new ReceiveCallbackManager<T>();
		bluetoothConnection = new BluetoothConnection();
		this.device = device;
		this.secure = secure;
		gson = new Gson();
	}
	
	public void start() { // TODO
		bluetoothConnection.start(bluetoothConnectionCallback);
	}

	@Override
	public void connect(final PersistentCallback callback) {
		add(callback);
		start();
		if(device != null) {
			bluetoothConnection.connect(device, secure, bluetoothConnectionCallback);
		}
	}
	
	@Override
	public boolean add(PersistentCallback callback) {
		return pcallbacks.add(callback);
	}
	
	@Override
	public boolean remove(PersistentCallback callback) {
		return pcallbacks.remove(callback);
	}

	@Override
	public void disconnect() {
		bluetoothConnection.stop();
		connected = false;
	}

	@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	public void send(T model, SendCallback<T> callback) {
		// TODO acknowledge and callback
		String message = gson.toJson(model, getType());
		byte[] out = message.getBytes();
		bluetoothConnection.write(out);
	}

	@Override
	public void receive(ReceiveCallback<T> callback) {
		add(callback);
	}
	
	@Override
	public boolean add(ReceiveCallback<T> callback) {
		return rcallbacks.add(callback);
	}
	
	@Override
	public boolean remove(ReceiveCallback<T> callback) {
		return rcallbacks.remove(callback);
	}
	
	private BluetoothConnectionCallback bluetoothConnectionCallback = new BluetoothConnectionCallback() {

		@Override
		public void onConnection(BluetoothConnection connection) {
			connected = true;
			pcallbacks.onConnected();
		}

		@Override
		public void onFailed(BluetoothConnection connection) {
			connected = false;
			pcallbacks.onDisconnected(); // TODO
		}

		@Override
		public void onLost(BluetoothConnection connection) {
			connected = false;
			pcallbacks.onDisconnected(); // TODO
		}

		@Override
		public void onRecieve(int bytes, byte[] buffer) {
			try {
				if(bytes > 0 && buffer != null && buffer.length >= 1) {
					String message = new String(buffer, 0 , bytes);
					T model = gson.fromJson(message, getType());
					rcallbacks.onReceive(model);
				}
			} catch(ClassCastException e) {
				// TODO
			} catch(Exception e) {
				// TODO
			}
		}
	};

	public Type getType() {
		return getTypeToken().getType();
	}
}
