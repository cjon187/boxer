package com.example.win7cmurder.boxer;

/**
 * Created by win7Cmurder on 7/16/2015.
 */
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
public class MyLocationListener implements LocationListener{
    public static double latitude;
    public static double longitude;
    @Override
    public void onLocationChanged(Location loc) {
        loc.getLatitude();
        loc.getLongitude();
        latitude=loc.getLatitude();
        longitude=loc.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}