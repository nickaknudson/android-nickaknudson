package com.nickaknudson.android.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public abstract class BootReceiver extends BroadcastReceiver {
	
	static public IntentFilter getFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_BOOT_COMPLETED);
		filter.addAction(Intent.ACTION_SHUTDOWN);
		return filter;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// get the action
		String action = intent.getAction();
		// Intent.ACTION_BOOT_COMPLETED
		if(action.equals(Intent.ACTION_BOOT_COMPLETED)) {
			onBootCompleted(context, intent);
		}
		// Intent.ACTION_SHUTDOWN
		if(action.equals(Intent.ACTION_SHUTDOWN)) {
			onShutdown(context, intent);
		}
	}
	
	protected abstract void onBootCompleted(Context context, Intent intent);
	protected abstract void onShutdown(Context context, Intent intent);

}