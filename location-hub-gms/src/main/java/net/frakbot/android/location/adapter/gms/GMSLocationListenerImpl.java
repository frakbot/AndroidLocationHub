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
