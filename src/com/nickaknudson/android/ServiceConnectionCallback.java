package com.nickaknudson.android;

import android.content.ComponentName;
import android.os.IBinder;

public interface ServiceConnectionCallback<T extends IBinder> {
	public void onServiceConnected(ComponentName name, T binder);
	public void onServiceDisconnected(ComponentName name);
}
