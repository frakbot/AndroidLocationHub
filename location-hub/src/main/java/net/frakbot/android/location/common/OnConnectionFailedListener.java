package net.frakbot.android.location.common;

/**
 * Provides callbacks for scenarios that result in a failed attempt to connect the client to the
 * service. See {@link net.frakbot.android.location.common.ConnectionResult} for a list of error
 * codes and suggestions for resolution.
 */
public interface OnConnectionFailedListener {
    public abstract void onConnectionFailed (ConnectionResult result);
}
