/*
 * Copyright 2014 Frakbot (Francesco Pontillo, Sebastiano Poggi)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.frakbot.android.location.adapter.gms;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

import net.frakbot.android.location.LocationHubAdapter;
import net.frakbot.android.location.common.ConnectionCallbacks;
import net.frakbot.android.location.common.LocationHubListener;
import net.frakbot.android.location.common.LocationHubRequest;
import net.frakbot.android.location.common.OnConnectionFailedListener;

import java.util.HashMap;

/**
 * Adapter for the GMS {@link com.google.android.gms.location.LocationClient}.
 */
public class GMSLocationHubAdapter extends LocationHubAdapter {

    private LocationClient mLocationClient;

    // Maps for holding custom listeners
    private HashMap<ConnectionCallbacks, GMSConnectionCallbacksImpl> mConnectionCallbacks;
    private HashMap<OnConnectionFailedListener, GMSOnConnectionFailedListenerImpl> mConnectionFailedListeners;
    private HashMap<LocationHubListener, GMSLocationListenerImpl> mListeners;

    public GMSLocationHubAdapter() {
        mConnectionCallbacks = new HashMap<ConnectionCallbacks, GMSConnectionCallbacksImpl>();
        mConnectionFailedListeners = new HashMap<OnConnectionFailedListener, GMSOnConnectionFailedListenerImpl>();
        mListeners = new HashMap<LocationHubListener, GMSLocationListenerImpl>();
    }

    @Override
    protected void setup(Context context, ConnectionCallbacks callbacks, OnConnectionFailedListener connectionFailedListener, Bundle bundle) {
        GMSConnectionCallbacksImpl realConnectionCallbacks = putConnectionCallbacks(callbacks);
        GMSOnConnectionFailedListenerImpl realConnectionFailedListener = putConnectionFailedListener(connectionFailedListener);

        mLocationClient = new LocationClient(context,
                realConnectionCallbacks,
                realConnectionFailedListener);
    }

    @Override
    public boolean isServiceAvailable(Context context) {
        return (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS);
    }

    @Override
    public String getAdapterName() {
        return "Google Play Services";
    }

    @Override
    public void connect() {
        mLocationClient.connect();
    }

    @Override
    public void disconnect() {
        mLocationClient.disconnect();
    }

    @Override
    public Location getLastLocation() {
        return mLocationClient.getLastLocation();
    }

    @Override
    public boolean isConnected() {
        return mLocationClient.isConnected();
    }

    @Override
    public boolean isConnecting() {
        return mLocationClient.isConnecting();
    }

    @Override
    public void registerConnectionCallbacks(ConnectionCallbacks listener) {
        // If the listener is not registered yet, add it to the list
        if (!isConnectionCallbacksRegistered(listener)) {
            GMSConnectionCallbacksImpl realListener = putConnectionCallbacks(listener);
            mLocationClient.registerConnectionCallbacks(realListener);
        }
    }

    @Override
    public void unregisterConnectionCallbacks(ConnectionCallbacks listener) {
        // get the actual listener implementation
        GMSConnectionCallbacksImpl realListener = mConnectionCallbacks.get(listener);
        // deregister it from the real client and remove it from the hashmap
        mLocationClient.unregisterConnectionCallbacks(realListener);
        mConnectionCallbacks.remove(listener);
    }

    @Override
    public boolean isConnectionCallbacksRegistered(ConnectionCallbacks listener) {
        GMSConnectionCallbacksImpl realListener = mConnectionCallbacks.get(listener);
        return mLocationClient.isConnectionCallbacksRegistered(realListener);
    }

    @Override
    public void registerConnectionFailedListener(OnConnectionFailedListener listener) {
        // If the listener is not registered yet, add it to the list
        if (!isConnectionFailedListenerRegistered(listener)) {
            GMSOnConnectionFailedListenerImpl realListener = putConnectionFailedListener(listener);
            mLocationClient.registerConnectionFailedListener(realListener);
        }
    }

    @Override
    public void unregisterConnectionFailedListener(OnConnectionFailedListener listener) {
        // get the actual listener implementation
        GMSOnConnectionFailedListenerImpl realListener = mConnectionFailedListeners.get(listener);
        // deregister it from the real client and remove it from the hashmap
        mLocationClient.unregisterConnectionFailedListener(realListener);
        mConnectionFailedListeners.remove(listener);
    }

    @Override
    public boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener listener) {
        GMSOnConnectionFailedListenerImpl realListener = mConnectionFailedListeners.get(listener);
        return mLocationClient.isConnectionFailedListenerRegistered(realListener);
    }

    @Override
    public void setMockMode(boolean isMockMode) throws SecurityException {
        mLocationClient.setMockMode(isMockMode);
    }

    @Override
    public void setMockLocation(Location mockLocation) throws SecurityException {
        mLocationClient.setMockLocation(mockLocation);
    }

    @Override
    public void requestLocationUpdates(LocationHubRequest request, LocationHubListener listener) {
        LocationRequest locationRequest = requestFromHub(request);
        GMSLocationListenerImpl realListener = putLocationListener(listener);
        mLocationClient.requestLocationUpdates(locationRequest, realListener);
    }

    @Override
    public void removeLocationUpdates(LocationHubListener listener) {
        GMSLocationListenerImpl realListener = mListeners.get(listener);
        mLocationClient.removeLocationUpdates(realListener);
        mListeners.remove(listener);
    }

    /**
     * Puts a generic {@link net.frakbot.android.location.common.ConnectionCallbacks} into the
     * map used to retrieve the actual callback implementation.
     *
     * @param callbacks The {@link net.frakbot.android.location.common.ConnectionCallbacks} callback
     *                  object for the connection states.
     * @return A {@link net.frakbot.android.location.adapter.gms.GMSConnectionCallbacksImpl} callback
     * implementation.
     */
    private GMSConnectionCallbacksImpl putConnectionCallbacks(ConnectionCallbacks callbacks) {
        if (callbacks != null) {
            GMSConnectionCallbacksImpl realConnectionCallbacks = new GMSConnectionCallbacksImpl(callbacks);
            mConnectionCallbacks.put(callbacks, realConnectionCallbacks);
            return realConnectionCallbacks;
        }
        return null;
    }

    /**
     * Puts a generic {@link net.frakbot.android.location.common.OnConnectionFailedListener} into the
     * map used to retrieve the actual listener implementation.
     *
     * @param listener The {@link net.frakbot.android.location.common.OnConnectionFailedListener} listener
     *                  object for the connection failure.
     * @return A {@link net.frakbot.android.location.adapter.gms.GMSOnConnectionFailedListenerImpl} listener
     * implementation.
     */
    private GMSOnConnectionFailedListenerImpl putConnectionFailedListener(OnConnectionFailedListener listener) {
        if (listener != null) {
            GMSOnConnectionFailedListenerImpl realConnectionFailedListener = new GMSOnConnectionFailedListenerImpl(listener);
            mConnectionFailedListeners.put(listener, realConnectionFailedListener);
            return realConnectionFailedListener;
        }
        return null;
    }

    /**
     * Puts a generic {@link net.frakbot.android.location.common.LocationHubListener} into the
     * map used to retrieve the actual listener implementation.
     *
     * @param listener The {@link net.frakbot.android.location.common.LocationHubListener} listener
     *                  object for the location updates.
     * @return A {@link net.frakbot.android.location.adapter.gms.GMSLocationListenerImpl} listener
     * implementation.
     */
    private GMSLocationListenerImpl putLocationListener(LocationHubListener listener) {
        if (listener != null) {
            GMSLocationListenerImpl realLocationListener = new GMSLocationListenerImpl(listener);
            mListeners.put(listener, realLocationListener);
            return realLocationListener;
        }
        return null;
    }

    /**
     * Builds a {@link com.google.android.gms.location.LocationRequest} from the custom implementation
     * of {@link net.frakbot.android.location.common.LocationHubRequest}.
     *
     * This method basically maps the handled property to the GMS ones.
     *
     * @param request The original {@link net.frakbot.android.location.common.LocationHubRequest} to convert.
     * @return A converted and ready to use {@link com.google.android.gms.location.LocationRequest}.
     */
    private LocationRequest requestFromHub(LocationHubRequest request) {
        LocationRequest locationRequest = LocationRequest.create();

        // Power
        if (request.getPriority() == LocationHubRequest.PRIORITY_NO_POWER) {
            locationRequest.setPriority(LocationRequest.PRIORITY_NO_POWER);
        } else if (request.getPriority() == LocationHubRequest.PRIORITY_LOW_POWER) {
            locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        } else if (request.getPriority() == LocationHubRequest.PRIORITY_BALANCED_POWER_ACCURACY) {
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        } else if (request.getPriority() == LocationHubRequest.PRIORITY_HIGH_ACCURACY) {
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }

        // Interval
        locationRequest.setInterval(request.getInterval());

        // Fastest interval
        locationRequest.setFastestInterval(request.getFastestInterval());

        // Smallest displacement
        locationRequest.setSmallestDisplacement(request.getSmallestDisplacement());

        return locationRequest;
    }
}
