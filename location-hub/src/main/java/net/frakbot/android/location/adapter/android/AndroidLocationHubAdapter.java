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

package net.frakbot.android.location.adapter.android;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;

import net.frakbot.android.location.LocationHubAdapter;
import net.frakbot.android.location.common.ConnectionCallbacks;
import net.frakbot.android.location.common.LocationHubListener;
import net.frakbot.android.location.common.LocationHubRequest;
import net.frakbot.android.location.common.OnConnectionFailedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Adapter for the Android built-in {@link android.location.LocationManager}.
 */
public class AndroidLocationHubAdapter extends LocationHubAdapter {

    private Context mContext;
    private LocationManager mLocationManager;
    private String mLocationProvider;
    private Location mLastKnownLocation;

    private boolean isConnected = false;
    private boolean isConnecting = false;
    private boolean isMock = false;

    private List<ConnectionCallbacks> mConnectionCallbacksList;
    private List<OnConnectionFailedListener> mConnectionFailedListenerList;
    private HashMap<LocationHubListener, AndroidLocationListenerImpl> mListeners;

    public AndroidLocationHubAdapter() {
        mConnectionCallbacksList = new ArrayList<ConnectionCallbacks>();
        mConnectionFailedListenerList = new ArrayList<OnConnectionFailedListener>();
        mListeners = new HashMap<LocationHubListener, AndroidLocationListenerImpl>();
    }

    @Override
    protected void setup(Context context, ConnectionCallbacks callbacks, OnConnectionFailedListener connectionFailedListener, Bundle bundle) {
        mContext = context;
        if (callbacks != null) {
            mConnectionCallbacksList.add(callbacks);
        }
        if (connectionFailedListener != null) {
            mConnectionFailedListenerList.add(connectionFailedListener);
        }
        // Get the location manager
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public boolean isServiceAvailable(Context context) {
        return true;
    }

    @Override
    public void connect() {
        // Just for the sake of clarity, isConnecting could be set asynchronously
        isConnecting = true;
        isConnected = true;
        isConnecting = false;
        for (ConnectionCallbacks callbacks : mConnectionCallbacksList) {
            callbacks.onConnected(null);
        }
        // TODO: catch and handle a null provider --> IllegalArgumentException follows
        updateLocation();
    }

    @Override
    public void disconnect() {
        isConnected = false;
        removeAllLocationUpdates();
        for (ConnectionCallbacks callbacks : mConnectionCallbacksList) {
            callbacks.onDisconnected();
        }
    }

    @Override
    public Location getLastLocation() {
        return updateLocation();
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public boolean isConnecting() {
        return isConnecting;
    }

    @Override
    public void registerConnectionCallbacks(ConnectionCallbacks listener) {
        if (listener == null) {
            return;
        }
        // If the listener is not registered yet, add it to the list
        if (!isConnectionCallbacksRegistered(listener)) {
            mConnectionCallbacksList.add(listener);
        }
        // If the Adapter is already connected, call the listener's onConnected method
        if (this.isConnected()) {
            listener.onConnected(null);
        }
    }

    @Override
    public void unregisterConnectionCallbacks(ConnectionCallbacks listener) {
        mConnectionCallbacksList.remove(listener);
    }

    @Override
    public boolean isConnectionCallbacksRegistered(ConnectionCallbacks listener) {
        // The list of connection callbacks holds all of the registered listeners
        return mConnectionCallbacksList.contains(listener);
    }

    @Override
    public void registerConnectionFailedListener(OnConnectionFailedListener listener) {
        // Register the listener if it is not null and if it is not registered yet
        if (listener != null && !isConnectionFailedListenerRegistered(listener)) {
            mConnectionFailedListenerList.add(listener);
        }
    }

    @Override
    public void unregisterConnectionFailedListener(OnConnectionFailedListener listener) {
        mConnectionFailedListenerList.remove(listener);
    }

    @Override
    public boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener listener) {
        // The list of connection failed listeners holds all of the registered failure listeners
        return mConnectionFailedListenerList.contains(listener);
    }

    @Override
    public void setMockMode(boolean isMockMode) throws SecurityException {
        if (isMockMode == isMock) {
            return;
        }
        isMock = isMockMode;
        if (isMock) {
            if (Settings.Secure.getInt(mContext.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) == 0) {
                throw new SecurityException(
                        "Mock locations are currently disabled in Settings. " +
                                "You can't use mock mode without enbabling mock locations.");
            }
            if (mContext.checkCallingOrSelfPermission(Manifest.permission.ACCESS_MOCK_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                throw new SecurityException(
                        "Current context does not have the android.permission.ACCESS_MOCK_LOCATION permission.");
            }
            mLocationProvider = MOCK_PROVIDER;
            if (mLocationManager.getProvider(MOCK_PROVIDER) == null) {
                mLocationManager.addTestProvider(
                        mLocationProvider, false, false, false, false, false, false, false,
                        Criteria.POWER_LOW, Criteria.ACCURACY_FINE);
                mLocationManager.setTestProviderEnabled(mLocationProvider, true);
            }
        } else {
            if (mLocationManager.getProvider(MOCK_PROVIDER) != null) {
                mLocationManager.setTestProviderEnabled(mLocationProvider, false);
                mLocationManager.removeTestProvider(mLocationProvider);
            }
        }
    }

    @Override
    public void setMockLocation(Location mockLocation) throws SecurityException {
        if (!isMock) {
            throw new SecurityException("You can't set a mock location if the mock mode is disabled.");
        }
        mLocationManager.setTestProviderLocation(mLocationProvider, mockLocation);
    }

    @Override
    public void requestLocationUpdates(LocationHubRequest request, LocationHubListener listener) {
        AndroidLocationListenerImpl realListener = new AndroidLocationListenerImpl(listener);
        mListeners.put(listener, realListener);
        // build the Criteria from the given request
        Criteria criteria = criteriaFromRequest(request);
        // get only the available providers, given the built request criteria
        mLocationProvider = getBestProvider(criteria, true);
        // request location updates with given criteria
        mLocationManager.requestLocationUpdates(
                mLocationProvider, request.getInterval(), request.getSmallestDisplacement(), realListener);
    }

    @Override
    public void removeLocationUpdates(LocationHubListener listener) {
        AndroidLocationListenerImpl realListener = mListeners.get(listener);
        mListeners.remove(listener);
        mLocationManager.removeUpdates(realListener);
    }

    /**
     * Removes all of the location updates.
     * For internal use only, this method will be called before disconnecting.
     */
    protected void removeAllLocationUpdates() {
        for (LocationHubListener hubListener : mListeners.keySet()) {
            removeLocationUpdates(hubListener);
        }
    }

    @Override
    public String getAdapterName() {
        return "Android Default Location Hub Adapter";
    }

    /**
     * Builds a {@link android.location.Criteria} object from a {@link net.frakbot.android.location.common.LocationHubRequest}
     * that holds the requirements for the {@link android.location.Location} the caller wants.
     * <p/>
     * You can override this method and implement your own {@link net.frakbot.android.location.common.LocationHubRequest}
     * to {@link android.location.Criteria} transformation.
     *
     * @param request The original {@link net.frakbot.android.location.common.LocationHubRequest}
     *                with all of the desired requirements of {@link android.location.Location}.
     * @return A {@link android.location.Criteria} with a good approximation of location requirements.
     */
    protected Criteria criteriaFromRequest(LocationHubRequest request) {
        Criteria criteria = new Criteria();
        if (request.getPriority() == LocationHubRequest.PRIORITY_NO_POWER) {
            // w/ PRIORITY_NO_POWER, also require ACCURACY_COARSE
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        } else if (request.getPriority() == LocationHubRequest.PRIORITY_LOW_POWER) {
            // on PRIORITY_LOW_POWER, don't care about accuracy
            criteria.setPowerRequirement(Criteria.POWER_LOW);
        } else if (request.getPriority() == LocationHubRequest.PRIORITY_BALANCED_POWER_ACCURACY) {
            // on PRIORITY_BALANCED_POWER_ACCURACY, don't care about accuracy
            criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        } else if (request.getPriority() == LocationHubRequest.PRIORITY_HIGH_ACCURACY) {
            // w/ PRIORITY_HIGH_ACCURACY, also require ACCURACY_FINE
            criteria.setPowerRequirement(Criteria.POWER_HIGH);
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
        }
        return criteria;
    }

    /**
     * Update the location from the {@link android.location.LocationManager}. If the retrieved
     * {@link android.location.Location} is null, it returns the previously cached one.
     *
     * @return The last known {@link android.location.Location}. It can be null if no location data is available yet.
     */
    private Location updateLocation() {
        // If the location provider is null, try to get the most approximate location using low power
        if (mLocationProvider == null) {
            Criteria criteria = new Criteria();
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            criteria.setAccuracy(Criteria.ACCURACY_LOW);
            mLocationProvider = mLocationManager.getBestProvider(criteria, true);
        }
        // TODO: catch and handle a null provider
        Location newLocation = mLocationManager.getLastKnownLocation(mLocationProvider);
        if (newLocation != null) {
            mLastKnownLocation = newLocation;
        }
        return mLastKnownLocation;
    }

    /**
     * Custom implementation for retrieving the best provider, considering the case that a mock
     * provider has been requested.
     * <p/>
     * If a mock provider was set, this will always be used until the mock option is set to false.
     *
     * @param criteria    The {@link android.location.Criteria} object.
     * @param enabledOnly Whether to only get an enabled provider.
     * @return The desired {@link java.lang.String} name of the provider.
     */
    private String getBestProvider(Criteria criteria, boolean enabledOnly) {
        if (isMock) {
            return MOCK_PROVIDER;
        }
        return mLocationManager.getBestProvider(criteria, true);
    }
}
