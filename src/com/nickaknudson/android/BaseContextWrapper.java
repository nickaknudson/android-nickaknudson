package com.nickaknudson.android;

import android.content.Context;

public class BaseContextWrapper {
	
	private Context base;

	public BaseContextWrapper(Context base) {
		setContext(base);
	}

	protected Context getContext() {
		return base;
	}

	protected void setContext(Context context) {
		base = context;
	}
}
