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

import android.location.Location;

import net.frakbot.android.location.common.ConnectionCallbacks;
import net.frakbot.android.location.common.LocationHubListener;
import net.frakbot.android.location.common.LocationHubRequest;
import net.frakbot.android.location.common.OnConnectionFailedListener;

/**
 * Interface for both the {@link net.frakbot.android.location.LocationHub} and the generic
 * {@link net.frakbot.android.location.LocationHubAdapter}.
 * All of the contained methods provide access to the underlying location provider/s.
 */
public interface ILocationHub {

    /**
     * Connects to a {@link LocationHubAdapter}. This method
     * returns immediately, and connects to the service in the background. If the connection is successful,
     * {@link net.frakbot.android.location.common.ConnectionCallbacks#onConnected(android.os.Bundle)} is called.
     * On a failure, {@link net.frakbot.android.location.common.OnConnectionFailedListener#onConnectionFailed(net.frakbot.android.location.common.ConnectionResult)} is called.
     */
    public void connect();

    /**
     * Disconnects from the {@link net.frakbot.android.location.LocationHubAdapter} implementation.
     * This method returns immediately.
     */
    public void disconnect();

    /**
     * Returns the best most recent location currently available.
     * <p/>
     * If a location is not available, which should happen very rarely, null will be returned.
     * The best accuracy available while respecting the location permissions will be returned.
     * <p/>
     * This method provides a simplified way to get location. It is particularly well suited for
     * applications that do not require an accurate location and that do not want to maintain extra
     * logic for location updates.
     */
    public Location getLastLocation();

    /**
     * Checks if the client is currently connected to the service, so that requests to other methods
     * will succeed. Applications should guard client actions caused by the user with a call to this method.
     *
     * @return true if the client is connected to the service.
     */
    public boolean isConnected();

    /**
     * Checks if the client is attempting to connect to the service.
     *
     * @return true if the client is attempting to connect to the service.
     */
    public boolean isConnecting();

    /**
     * Registers a listener to receive connection events from this {@link LocationHubAdapter}.
     * If the Adapter is already connected, the listener's {@link net.frakbot.android.location.common.ConnectionCallbacks#onConnected(android.os.Bundle)} method will be called immediately.
     * Applications should balance calls to this method with calls to {@link LocationHubAdapter#unregisterConnectionCallbacks(net.frakbot.android.location.common.ConnectionCallbacks)} to avoid leaking resources.
     * If the specified listener is already registered to receive connection events, this method will not add a duplicate entry for the same listener, but will still call the listener's {@link net.frakbot.android.location.common.ConnectionCallbacks#onConnected(android.os.Bundle)} method if currently connected.
     * Note that the order of messages received here may not be stable, so clients should not rely on the order that multiple listeners receive events in.
     *
     * @param listener the listener where the results of the asynchronous {@link LocationHubAdapter#connect()} call are delivered.
     */
    public abstract void registerConnectionCallbacks(ConnectionCallbacks listener);

    /**
     * Removes a connection listener from this {@link LocationHubAdapter}. Note that removing a listener does not generate any callbacks.
     * If the specified listener is not currently registered to receive connection events, this method will have no effect.
     *
     * @param listener the listener to unregister.
     */
    public abstract void unregisterConnectionCallbacks(ConnectionCallbacks listener);

    /**
     * Returns true if the specified listener is currently registered to receive connection events.
     *
     * @param listener The listener to check for.
     * @return true if the specified listener is currently registered to receive connection events.
     */
    public abstract boolean isConnectionCallbacksRegistered(ConnectionCallbacks listener);

    /**
     * Registers a listener to receive connection events from this {@link LocationHubAdapter}.
     * Unlike {@link LocationHubAdapter#registerConnectionCallbacks(net.frakbot.android.location.common.ConnectionCallbacks)}, if the service is not already connected, the listener's {@link net.frakbot.android.location.common.OnConnectionFailedListener#onConnectionFailed(net.frakbot.android.location.common.ConnectionResult)} method will not be called immediately.
     * Applications should balance calls to this method with calls to {@link LocationHubAdapter#unregisterConnectionCallbacks(net.frakbot.android.location.common.ConnectionCallbacks)} to avoid leaking resources.
     * If the specified listener is already registered to receive connection failed events, this method will not add a duplicate entry for the same listener.
     * Note that the order of messages received here may not be stable, so clients should not rely on the order that multiple listeners receive events in.
     *
     * @param listener the listener where the results of the asynchronous {@link LocationHubAdapter#connect()} call are delivered.
     */
    public abstract void registerConnectionFailedListener(OnConnectionFailedListener listener);

    /**
     * Removes a connection failed listener from the {@link LocationHubAdapter}. Note that removing a listener does not generate any callbacks.
     * If the specified listener is not currently registered to receive connection failed events, this method will have no effect.
     *
     * @param listener the listener to unregister.
     */
    public abstract void unregisterConnectionFailedListener(OnConnectionFailedListener listener);

    /**
     * Returns true if the specified listener is currently registered to receive connection failed events.
     *
     * @param listener The listener to check for.
     * @return true if the specified listener is currently registered to receive connection failed events.
     */
    public abstract boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener listener);

    /**
     * Sets whether or not the location provider is in mock mode.
     * <p/>
     * The client must remain connected in order for mock mode to remain active. If the client dies the
     * system will return to its normal state.
     * <p/>
     * Calls are not nested, and mock mode will be set directly regardless of previous calls.
     *
     * @param isMockMode If true the location provider will be set to mock mode.
     *                   If false it will be returned to its normal state.
     * @throws java.lang.SecurityException if the ACCESS_MOCK_LOCATION permission is not present or the
     *                                     {@link android.provider.Settings.Secure#ALLOW_MOCK_LOCATION}
     *                                     system setting is not enabled.
     */
    public abstract void setMockMode(boolean isMockMode) throws SecurityException;

    /**
     * Sets the mock location to be used. This location will be used in place of any actual locations
     * from the underlying providers.
     * <p/>
     * {@link net.frakbot.android.location.ILocationHub#setMockMode(boolean)} must be called and set
     * to true prior to calling this method.
     * <p/>
     * Care should be taken in specifying the timestamps as many applications require them to be
     * monotonically increasing.
     *
     * @param mockLocation The mock location. Must have a minimum number of fields set to be
     *                     considered a valild location, as per documentation in the
     *                     {@link android.location.Location} class.
     * @throws java.lang.SecurityException if the ACCESS_MOCK_LOCATION permission is not present or the
     *                                     {@link android.provider.Settings.Secure#ALLOW_MOCK_LOCATION}
     *                                     system setting is not enabled.
     */
    public abstract void setMockLocation(Location mockLocation) throws SecurityException;

    /**
     * TODO: write doc
     * TODO: handle no provider available exception
     *
     * @param request
     * @param listener
     */
    public abstract void requestLocationUpdates(LocationHubRequest request, LocationHubListener listener);

    /**
     * TODO: write doc
     *
     * @param listener
     */
    public abstract void removeLocationUpdates(LocationHubListener listener);

}
