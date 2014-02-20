package net.frakbot.android.location.common;

import android.location.Location;

/**
 * Created by fpontillo on 17/02/14.
 */
public abstract class LocationHubListener {
    public abstract void onLocationChanged(Location location);
}
