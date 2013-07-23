package com.nickaknudson.android;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class ServiceConnectionManager<T extends IBinder> extends ContextWrapper implements ServiceConnection {
	private static final String TAG = ServiceConnectionManager.class.getSimpleName();
	
	private boolean bound = false;
	private ServiceConnectionCallback<T> callback;
	private Class<?> cls;
	private Integer flags;
	
	public ServiceConnectionManager(Context base) {
		super(base);
	}

	public ServiceConnectionManager(Context base, Class<?> cls) {
		super(base);
		setService(cls);
	}

	public ServiceConnectionManager<T> setService(Class<?> cls) {
		this.cls = cls;
		return this;
	}

	public ServiceConnectionManager<T> setFlags(Integer flags) {
		this.flags = flags;
		return this;
	}

	public ServiceConnectionManager<T> setCallback(ServiceConnectionCallback<T> callback) {
		this.callback = callback;
		return this;
	}

	public ServiceConnectionManager<T> bind() {
		assert cls != null;
		assert callback != null;
		Intent intent = new Intent(this, cls);
		bindService(intent, this, flags);
		return this;
	}

	public boolean isBound() {
		return bound;
	}
	
	public void unbind() {
		if(bound) {
			bound = false;
			unbindService(this);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onServiceConnected(ComponentName name, IBinder service) {
		bound = true;
		callback.onServiceConnected(name, (T) service);
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		bound = false;
		callback.onServiceDisconnected(name);
	}
}
