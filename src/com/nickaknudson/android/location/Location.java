/**
 * 
 */
package com.nickaknudson.android.location;

import android.os.Bundle;
import android.util.Printer;

import com.nickaknudson.mva.Model;

/**
 * @author nick
 *
 */
public class Location extends Model<Location> {
	
	private android.location.Location location;

	public android.location.Location getLocation() {
		return location;
	}

	/* (non-Javadoc)
	 * @see com.nickaknudson.mva.Model#set(com.nickaknudson.mva.Model)
	 */
	@Override
	public void set(Location model) {
		location = model.getLocation();
		changed();
	}

	public void set(android.location.Location location) {
		this.location = location;
		changed();
	}

	public void set(String provider) {
		set(new android.location.Location(provider));
	}
	
	public float bearingTo(Location dest) {
		return location.bearingTo(dest.getLocation());
	}
	
	public static double convert(String coordinate) {
		return android.location.Location.convert(coordinate);
	}
	
	public static String convert(double coordinate, int outputType) {
		return android.location.Location.convert(coordinate, outputType);
	}
	
	public int describeContents() {
		return location.describeContents();
	}
	
	public static void distanceBetween(double startLatitude, double startLongitude, double endLatitude, double endLongitude, float[] results) {
		android.location.Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, results);
	}
	
	public float  distanceTo(Location dest) {
		return location.distanceTo(dest.getLocation());
	}
	
	public void dump(Printer pw, String prefix) {
		location.dump(pw, prefix);
	}
	
	public float getAccuracy() {
		return location.getAccuracy();
	}
	
	public double getAltitude() {
		return location.getAltitude();
	}
	
	public float getBearing() {
		return location.getBearing();
	}
	public 
	long getElapsedRealtimeNanos() {
		return location.getElapsedRealtimeNanos();
	}
	
	public Bundle getExtras() {
		return location.getExtras();
	}
	
	public double getLatitude() {
		return location.getLatitude();
	}
	
	public double getLongitude() {
		return location.getLongitude();
	}
	
	public String getProvider() {
		return location.getProvider();
	}
	
	public float getSpeed() {
		return location.getSpeed();
	}
	
	public long getTime() {
		return location.getTime();
	}
	
	public boolean hasAccuracy() {
		return location.hasAccuracy();
	}
	
	public boolean hasAltitude() {
		return location.hasAltitude();
	}
	
	public boolean hasBearing() {
		return location.hasBearing();
	}
	
	public boolean hasSpeed() {
		return location.hasSpeed();
	}
	
	//boolean	 isFromMockProvider() {
	//	return location.isFromMockProvider();
	//}
}
