package net.frakbot.android.location.adapter.gms;

import android.location.Location;

import com.google.android.gms.location.LocationListener;

import net.frakbot.android.location.common.LocationHubListener;

/**
 * Listener implementation for the GMS {@link com.google.android.gms.location.LocationListener}.
 */
public class GMSLocationListenerImpl implements LocationListener {
    private LocationHubListener mHubListener;

    public GMSLocationListenerImpl(LocationHubListener hubListener) {
        mHubListener = hubListener;
    }

    @Override
    public void onLocationChanged(Location location) {
        mHubListener.onLocationChanged(location);
    }
}
