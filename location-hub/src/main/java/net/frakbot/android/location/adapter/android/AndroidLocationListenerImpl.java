package net.frakbot.android.location.adapter.android;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import net.frakbot.android.location.common.LocationHubListener;

/**
 * Listener implementation for the Android built-in {@link android.location.LocationListener}.
 */
public class AndroidLocationListenerImpl implements LocationListener {
    private LocationHubListener mHubListener;

    public AndroidLocationListenerImpl(LocationHubListener hubListener) {
        mHubListener = hubListener;
    }

    @Override
    public void onLocationChanged(Location location) {
        mHubListener.onLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // don't care (for now)
    }

    @Override
    public void onProviderEnabled(String provider) {
        // don't care (for now)
    }

    @Override
    public void onProviderDisabled(String provider) {
        // don't care (for now)
    }
}
