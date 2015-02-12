package com.pluslibrary.utils;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
/**
 * location으로 주소 얻기 
 * @author jeff
 *
 */
public class PlusAddressFinder {
	public static String doIt(Activity activity, Location location)
			throws IOException {
		if (location == null) {
			return "";
		}
		Geocoder gc = new Geocoder(activity);
		Address address = null;
		StringBuilder builder = new StringBuilder();
		List<Address> addresses = gc.getFromLocation(location.getLatitude(),
				location.getLongitude(), 1);
		if (addresses.size() > 0) {
			address = addresses.get(0);
		}

		builder.append(address.getAdminArea()).append(" ");
		builder.append(address.getLocality()).append(" ");
		builder.append(address.getThoroughfare()).append(" ");

		return builder.toString();
	}
}
