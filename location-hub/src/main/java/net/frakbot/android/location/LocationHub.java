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

package net.frakbot.android.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import net.frakbot.android.location.adapter.android.AndroidLocationHubAdapter;
import net.frakbot.android.location.common.ConnectionCallbacks;
import net.frakbot.android.location.common.LocationHubListener;
import net.frakbot.android.location.common.LocationHubRequest;
import net.frakbot.android.location.common.OnConnectionFailedListener;
import net.frakbot.android.location.resolver.DefaultLocationHubAdapterResolver;

/**
 * The LocationHub is the main entry point for location related APIs, such as location and geofence.
 * Use the LocationHub to:
 * <ul>
 * <li>Connect and disconnect.</li>
 * <li>Request/remove location update callbacks.</li>
 * <li>Request/remove geofences.</li>
 * </ul>
 * In order to establish a connection, call connect() and wait for the onConnected(android.os.Bundle) callback.
 */
public class LocationHub implements ILocationHub {

    private Context mContext;
    private ConnectionCallbacks mConnectionCallback;
    private OnConnectionFailedListener mConnectionFailedListener;
    private ILocationHubAdapterResolver mLocationAdapterResolver;

    private LocationHubAdapter mLocationAdapter;

    /**
     * Instantiate a new hub by relying on the default Android {@link android.location.LocationManager} only.
     *
     * @param context                  The reference {@link android.content.Context}.
     * @param connectionCallback       A {@link net.frakbot.android.location.common.ConnectionCallbacks} instance
     *                                 for handling connection/disconnection callbacks.
     * @param connectionFailedListener A {@link net.frakbot.android.location.common.OnConnectionFailedListener}
     *                                 listener for handling connection failures.
     */
    public LocationHub(Context context, ConnectionCallbacks connectionCallback, OnConnectionFailedListener connectionFailedListener) {
        this(context, connectionCallback, connectionFailedListener, LocationHub.getDefaultResolver(context));
    }

    /**
     * Instantiate a new hub with a custom {@link ILocationHubAdapterResolver} instance.
     * The instance of the resolver will manage all of the {@link LocationHubAdapter}s
     * and their selection.
     *
     * @param context                  The reference {@link android.content.Context}.
     * @param connectionCallback       A {@link net.frakbot.android.location.common.ConnectionCallbacks} instance
     *                                 for handling connection/disconnection callbacks.
     * @param connectionFailedListener A {@link net.frakbot.android.location.common.OnConnectionFailedListener}
     *                                 listener for handling connection failures.
     * @param customResolver           A custom {@link ILocationHubAdapterResolver} for
     *                                 selecting the appropriate {@link LocationHubAdapter}.
     */
    public LocationHub(Context context, ConnectionCallbacks connectionCallback, OnConnectionFailedListener connectionFailedListener, ILocationHubAdapterResolver customResolver) {
        mContext = context;
        mConnectionCallback = connectionCallback;
        mConnectionFailedListener = connectionFailedListener;
        mLocationAdapterResolver = customResolver;
        mLocationAdapter = mLocationAdapterResolver.getAdapter();
    }

    /**
     * Sets up and connects to a {@link LocationHubAdapter}.
     *
     * @param bundle A {@link android.os.Bundle} of data to set up the adapter with.
     * @see {@link LocationHub#connect()}
     */
    public void connect(Bundle bundle) {
        mLocationAdapter.setup(mContext, mConnectionCallback, mConnectionFailedListener, bundle);
        mLocationAdapter.connect();
    }

    /**
     * Sets up and connects to a {@link LocationHubAdapter}. This method
     * returns immediately, and connects to the service in the background. If the connection is successful,
     * {@link net.frakbot.android.location.common.ConnectionCallbacks#onConnected(android.os.Bundle)} is called.
     * On a failure, {@link net.frakbot.android.location.common.OnConnectionFailedListener#onConnectionFailed(net.frakbot.android.location.common.ConnectionResult)} is called.
     */
    @Override
    public void connect() {
        connect(null);
    }

    @Override
    public void disconnect() {
        mLocationAdapter.disconnect();
    }

    @Override
    public Location getLastLocation() {
        return mLocationAdapter.getLastLocation();
    }

    @Override
    public boolean isConnected() {
        return mLocationAdapter.isConnected();
    }

    @Override
    public boolean isConnecting() {
        return mLocationAdapter.isConnecting();
    }

    @Override
    public void registerConnectionCallbacks(ConnectionCallbacks listener) {
        mLocationAdapter.registerConnectionCallbacks(listener);
    }

    @Override
    public void unregisterConnectionCallbacks(ConnectionCallbacks listener) {
        mLocationAdapter.unregisterConnectionCallbacks(listener);
    }

    @Override
    public boolean isConnectionCallbacksRegistered(ConnectionCallbacks listener) {
        return mLocationAdapter.isConnectionCallbacksRegistered(listener);
    }

    @Override
    public void registerConnectionFailedListener(OnConnectionFailedListener listener) {
        mLocationAdapter.registerConnectionFailedListener(listener);
    }

    @Override
    public void unregisterConnectionFailedListener(OnConnectionFailedListener listener) {
        mLocationAdapter.unregisterConnectionFailedListener(listener);
    }

    @Override
    public boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener listener) {
        return mLocationAdapter.isConnectionFailedListenerRegistered(listener);
    }

    @Override
    public void setMockMode(boolean isMockMode) throws SecurityException {
        mLocationAdapter.setMockMode(isMockMode);
    }

    @Override
    public void setMockLocation(Location mockLocation) throws SecurityException {
        mLocationAdapter.setMockLocation(mockLocation);
    }

    @Override
    public void requestLocationUpdates(LocationHubRequest request, LocationHubListener listener) {
        mLocationAdapter.requestLocationUpdates(request, listener);
    }

    @Override
    public void removeLocationUpdates(LocationHubListener listener) {
        mLocationAdapter.removeLocationUpdates(listener);
    }

    public LocationHubAdapter getAdapterImpl() {
        return mLocationAdapter;
    }

    private static ILocationHubAdapterResolver getDefaultResolver(Context context) {
        ILocationHubAdapterResolver defaultResolver = new DefaultLocationHubAdapterResolver(context);
        defaultResolver.getAdapterList().add(new AndroidLocationHubAdapter());
        return defaultResolver;
    }
}
