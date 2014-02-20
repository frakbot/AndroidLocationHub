package net.frakbot.android.location.adapter.gms;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;

import net.frakbot.android.location.common.OnConnectionFailedListener;

/**
 * Implementation of {@link com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener}
 * for the {@link net.frakbot.android.location.adapter.gms.GMSLocationHubAdapter}.
 * <p/>
 * It dispatches connection and disconnection events to a custom
 * {@link net.frakbot.android.location.common.OnConnectionFailedListener} object.
 */
public class GMSOnConnectionFailedListenerImpl implements GooglePlayServicesClient.OnConnectionFailedListener {

    private OnConnectionFailedListener mListener;

    public GMSOnConnectionFailedListenerImpl(OnConnectionFailedListener listener) {
        mListener = listener;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // TODO: don't handle connection results for now
        mListener.onConnectionFailed(null);
    }
}
