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
