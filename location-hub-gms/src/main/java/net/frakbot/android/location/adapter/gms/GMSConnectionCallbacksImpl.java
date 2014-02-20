package net.frakbot.android.location.adapter.gms;

import android.os.Bundle;

import com.google.android.gms.common.GooglePlayServicesClient;

import net.frakbot.android.location.common.ConnectionCallbacks;

/**
 * Implementation of {@link com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks}
 * for the {@link net.frakbot.android.location.adapter.gms.GMSLocationHubAdapter}.
 * <p/>
 * It dispatches connection and disconnection events to a custom
 * {@link net.frakbot.android.location.common.ConnectionCallbacks} object.
 */
public class GMSConnectionCallbacksImpl implements GooglePlayServicesClient.ConnectionCallbacks {
    private ConnectionCallbacks mCallbacks;

    public GMSConnectionCallbacksImpl(ConnectionCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void onConnected(Bundle bundle) {
        mCallbacks.onConnected(bundle);
    }

    @Override
    public void onDisconnected() {
        mCallbacks.onDisconnected();
    }
}
