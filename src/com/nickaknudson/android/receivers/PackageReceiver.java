package com.nickaknudson.android.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

public abstract class PackageReceiver extends BroadcastReceiver {
	
	static public IntentFilter getFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
		filter.addAction(Intent.ACTION_PACKAGE_DATA_CLEARED);
		//filter.addAction(Intent.ACTION_PACKAGE_FIRST_LAUNCH);
		filter.addAction(Intent.ACTION_PACKAGE_FULLY_REMOVED);
		//filter.addAction(Intent.ACTION_PACKAGE_NEEDS_VERIFICATION);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
		filter.addAction(Intent.ACTION_PACKAGE_RESTARTED);
		//filter.addAction(Intent.ACTION_PACKAGE_VERIFIED);
	    filter.addDataScheme("package");
		return filter;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// get the action
		String action = intent.getAction();
		// Intent.ACTION_PACKAGE_ADDED
		if(action.equals(Intent.ACTION_PACKAGE_ADDED)) {
			Integer uid = intent.getIntExtra(Intent.EXTRA_UID, Integer.MIN_VALUE);
			String packageName = context.getPackageManager().getNameForUid(uid);
			Boolean replacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);
			onPackageAdded(context, intent, uid, packageName, replacing);
		}
		// Intent.ACTION_PACKAGE_CHANGED
		if(action.equals(Intent.ACTION_PACKAGE_CHANGED)) {
			Integer uid = intent.getIntExtra(Intent.EXTRA_UID, Integer.MIN_VALUE);
			String packageName = context.getPackageManager().getNameForUid(uid);
			String[] componentNameList = intent.getStringArrayExtra(Intent.EXTRA_CHANGED_COMPONENT_NAME_LIST);
			Boolean dontKillApp = intent.getBooleanExtra(Intent.EXTRA_DONT_KILL_APP, false);
			onPackageChanged(context, intent, uid, packageName, componentNameList, dontKillApp);
		}
		// Intent.ACTION_PACKAGE_DATA_CLEARED
		if(action.equals(Intent.ACTION_PACKAGE_DATA_CLEARED)) {
			Integer uid = intent.getIntExtra(Intent.EXTRA_UID, Integer.MIN_VALUE);
			String packageName = context.getPackageManager().getNameForUid(uid);
			onPackageDataCleared(context, intent, uid, packageName);
		}
		// Intent.ACTION_PACKAGE_FIRST_LAUNCH
		//if(action.equals(Intent.ACTION_PACKAGE_FIRST_LAUNCH)) {
		//	Integer uid = intent.getIntExtra(Intent.EXTRA_UID, Integer.MIN_VALUE);
		//	onPackageFirstLaunch(context, intent, uid);
		//}
		// Intent.ACTION_PACKAGE_FULLY_REMOVED
		if(action.equals(Intent.ACTION_PACKAGE_FULLY_REMOVED)) {
			Integer uid = intent.getIntExtra(Intent.EXTRA_UID, Integer.MIN_VALUE);
			String packageName = context.getPackageManager().getNameForUid(uid);
			onPackageFullyRemoved(context, intent, uid, packageName);
		}
		// Intent.ACTION_PACKAGE_NEEDS_VERIFICATION
		//if(action.equals(Intent.ACTION_PACKAGE_NEEDS_VERIFICATION)) {
		//	Integer uid = intent.getIntExtra(Intent.EXTRA_UID, Integer.MIN_VALUE);
		//	onPackageNeedsVerification(context, intent, uid);
		//}
		// Intent.ACTION_PACKAGE_REMOVED
		if(action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
			Integer uid = intent.getIntExtra(Intent.EXTRA_UID, Integer.MIN_VALUE);
			String packageName = context.getPackageManager().getNameForUid(uid);
			Boolean dataRemoved = intent.getBooleanExtra(Intent.EXTRA_DATA_REMOVED, false);
			Boolean replacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);
			onPackageRemoved(context, intent, uid, packageName, dataRemoved, replacing);
		}
		// Intent.ACTION_PACKAGE_REPLACED
		if(action.equals(Intent.ACTION_PACKAGE_REPLACED)) {
			Integer uid = intent.getIntExtra(Intent.EXTRA_UID, Integer.MIN_VALUE);
			String packageName = context.getPackageManager().getNameForUid(uid);
			onPackageReplaced(context, intent, uid, packageName);
		}
		// Intent.ACTION_PACKAGE_RESTARTED
		if(action.equals(Intent.ACTION_PACKAGE_RESTARTED)) {
			Integer uid = intent.getIntExtra(Intent.EXTRA_UID, Integer.MIN_VALUE);
			String packageName = context.getPackageManager().getNameForUid(uid);
			onPackageRestarted(context, intent, uid, packageName);
		}
		// Intent.ACTION_PACKAGE_VERIFIED
		//if(action.equals(Intent.ACTION_PACKAGE_VERIFIED)) {
		//	Integer uid = intent.getIntExtra(Intent.EXTRA_UID, Integer.MIN_VALUE);
		//	onPackageVerified(context, intent, uid);
		//}
	}

	public abstract void onPackageAdded(Context context, Intent intent, Integer uid, String packageName, Boolean replacing);
	public abstract void onPackageChanged(Context context, Intent intent, Integer uid, String packageName, String[] componentNameList, Boolean dontKillApp);
	public abstract void onPackageDataCleared(Context context, Intent intent, Integer uid, String packageName);
	public abstract void onPackageFullyRemoved(Context context, Intent intent, Integer uid, String packageName);
	public abstract void onPackageRemoved(Context context, Intent intent, Integer uid, String packageName, Boolean dataRemoved, Boolean replacing);
	public abstract void onPackageReplaced(Context context, Intent intent, Integer uid, String packageName);
	public abstract void onPackageRestarted(Context context, Intent intent, Integer uid, String packageName);
}
